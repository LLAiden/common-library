package com.common.media;

import android.media.AudioFormat;
import android.util.Log;

import androidx.camera.core.ImageProxy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class MultimediaHelper {

    private final static String TAG = "MediaUtil";

    public static Runnable convertPcm2Wav(File pcmFile, File wavFile, int sampleRateInHz, int channelConfig, int audioFormat) {
        return () -> {
            if (wavFile.exists()) {
                boolean delete = wavFile.delete();
                Log.e(TAG, "convertPcmToWav: delete: " + delete);
            }
            FileInputStream fileInputStream = null;
            FileOutputStream fileOutputStream = null;

            try {
                fileInputStream = new FileInputStream(pcmFile);
                fileOutputStream = new FileOutputStream(wavFile);

                long audioByteLen = fileInputStream.getChannel().size();
                long wavByteLen = audioByteLen + 36;

                addWavHeader(fileOutputStream, audioByteLen, wavByteLen, sampleRateInHz,
                        channelConfig, audioFormat);
                byte[] buffer = new byte[1024];
                while (fileInputStream.read(buffer) != -1) {
                    fileOutputStream.write(buffer);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fileInputStream != null) {
                        fileInputStream.close();
                    }
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public static void addWavHeader(FileOutputStream fileOutputStream, long audioByteLen, long wavByteLen, int sampleRateInHz, int channelConfig, int audioFormat) {
        byte[] header = new byte[44];

        // RIFF/WAVE header chunk
        header[0] = 'R';
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';
        header[4] = (byte) (wavByteLen & 0xff);
        header[5] = (byte) ((wavByteLen >> 8) & 0xff);
        header[6] = (byte) ((wavByteLen >> 16) & 0xff);
        header[7] = (byte) ((wavByteLen >> 24) & 0xff);

        //WAVE
        header[8] = 'W';
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';

        // 'fmt ' chunk 4 个字节
        header[12] = 'f';
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';
        // 4 bytes: size of 'fmt ' chunk（格式信息数据的大小 header[20] ~ header[35]）
        header[16] = 16;
        header[17] = 0;
        header[18] = 0;
        header[19] = 0;
        // format = 1 编码方式
        header[20] = 1;
        header[21] = 0;
        // 声道数目
        int channelSize = channelConfig == AudioFormat.CHANNEL_IN_MONO ? 1 : 2;
        header[22] = (byte) channelSize;
        header[23] = 0;
        // 采样频率
        header[24] = (byte) (sampleRateInHz & 0xff);
        header[25] = (byte) ((sampleRateInHz >> 8) & 0xff);
        header[26] = (byte) ((sampleRateInHz >> 16) & 0xff);
        header[27] = (byte) ((sampleRateInHz >> 24) & 0xff);
        // 每秒传输速率
        long byteRate = (long) audioFormat * sampleRateInHz * channelSize;
        header[28] = (byte) (byteRate & 0xff);
        header[29] = (byte) ((byteRate >> 8) & 0xff);
        header[30] = (byte) ((byteRate >> 16) & 0xff);
        header[31] = (byte) ((byteRate >> 24) & 0xff);
        // block align 数据库对齐单位，每个采样需要的字节数
        header[32] = (byte) (2 * 16 / 8);
        header[33] = 0;
        // bits per sample 每个采样需要的 bit 数
        header[34] = 16;
        header[35] = 0;

        //data chunk
        header[36] = 'd';
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';
        // pcm字节数
        header[40] = (byte) (audioByteLen & 0xff);
        header[41] = (byte) ((audioByteLen >> 8) & 0xff);
        header[42] = (byte) ((audioByteLen >> 16) & 0xff);
        header[43] = (byte) ((audioByteLen >> 24) & 0xff);

        try {
            fileOutputStream.write(header, 0, 44);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * YYYY YYYY   VU VU
     */
    public static byte[] yuv420ToNv21(ImageProxy image) {
        ImageProxy.PlaneProxy[] planes = image.getPlanes();
        ByteBuffer yBuffer = planes[0].getBuffer();
        ByteBuffer uBuffer = planes[1].getBuffer();
        ByteBuffer vBuffer = planes[2].getBuffer();
        int ySize = yBuffer.remaining();
        int uSize = uBuffer.remaining();
        int vSize = vBuffer.remaining();
        int size = image.getWidth() * image.getHeight();
        byte[] nv21 = new byte[size * 3 / 2];
        yBuffer.get(nv21, 0, ySize);
        vBuffer.get(nv21, ySize, vSize);

        byte[] u = new byte[uSize];
        uBuffer.get(u);

        //每隔开一位替换V，达到VU交替
        int pos = ySize + 1;
        for (int i = 0; i < uSize; i++) {
            if (i % 2 == 0) {
                nv21[pos] = u[i];
                pos += 2;
            }
        }
        return nv21;
    }


    /**
     * YYYY YYYY   UV  UV
     */
    public static byte[] yuv420ToNv12(ImageProxy image) {
        ImageProxy.PlaneProxy[] planes = image.getPlanes();
        ByteBuffer yBuffer = planes[0].getBuffer();
        ByteBuffer uBuffer = planes[1].getBuffer();
        ByteBuffer vBuffer = planes[2].getBuffer();
        int ySize = yBuffer.remaining();
        int uSize = uBuffer.remaining();
        int vSize = vBuffer.remaining();
        int size = image.getWidth() * image.getHeight();
        byte[] nv21 = new byte[size * 3 / 2];
        yBuffer.get(nv21, 0, ySize);
        uBuffer.get(nv21, ySize, uSize);

        byte[] v = new byte[vSize];
        vBuffer.get(v);

        //每隔开一位替换V，达到UV交替
        int pos = ySize + 1;
        for (int i = 0; i < uSize; i++) {
            if (i % 2 == 0) {
                nv21[pos] = v[i];
                pos += 2;
            }
        }
        return nv21;
    }
}
