package com.common.media;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@RequiresApi(api = Build.VERSION_CODES.ECLAIR)
public class AudioRecordProxy {

    private AudioRecord mAudioRecord;
    private final static String TAG = "AudioRecordProxy";
    private final int mBufferSize;
    private boolean isRecording;
    private final int mSampleRateInHz = 44100;

    public AudioRecordProxy(Context context) {
        int channelConfig = AudioFormat.CHANNEL_IN_MONO;
        int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
        mBufferSize = AudioRecord.getMinBufferSize(mSampleRateInHz, channelConfig, audioFormat);
        Log.e(TAG, "isInit: bufferSize: " + mBufferSize);
        //声音来源麦克风
        //MediaRecorder.AudioSource.MIC
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "AudioProxy: 缺少 Manifest.permission.RECORD_AUDIO 权限...");
        }
        mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, mSampleRateInHz, channelConfig, audioFormat, mBufferSize);
    }

    public int getSampleRateInHz() {
        return mSampleRateInHz;
    }

    public int getAudioRecordBufferSize() {
        return mBufferSize;
    }

    public boolean isInitStatus() {
        boolean audioStatus = mAudioRecord.getState() == AudioRecord.STATE_INITIALIZED;
        Log.e(TAG, "isInit: audioStatus: " + audioStatus);
        return audioStatus;
    }

    public void stopRecord() {
        Log.e(TAG, "stopRecord: ");
        isRecording = false;
        mAudioRecord.stop();
    }

    public Runnable startRecording(File file) {
        Log.e(TAG, "startRecording: ");
        isRecording = true;
        Runnable runnable = startRecordThread(file);
        mAudioRecord.startRecording();
        return runnable;
    }

    public void releaseAudioRecord() {
        Log.e(TAG, "releaseAudioRecord: ");
        if (isRecording) {
            isRecording = false;
            mAudioRecord.stop();
        }
        mAudioRecord.release();
        mAudioRecord = null;
    }

    public Runnable startRecordThread(File file) {
        return () -> {
            FileOutputStream fileOutputStream = null;
            int bufferSize = getAudioRecordBufferSize();
            final byte[] buffer = new byte[bufferSize];
            try {
                fileOutputStream = new FileOutputStream(file);
                while (isRecording) {
                    int readStatus = mAudioRecord.read(buffer, 0, bufferSize);
                    Log.d(TAG, "run: readStatus=" + readStatus);
                    fileOutputStream.write(buffer);
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "run: ", e);
            } finally {
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }
}