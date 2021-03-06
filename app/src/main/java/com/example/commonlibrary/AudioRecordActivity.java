package com.example.commonlibrary;

import android.media.AudioFormat;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.common.adapter.CommonAdapter;
import com.common.adapter.viewholder.ViewHolder;
import com.common.media.AudioRecordProxy;
import com.common.media.MultimediaHelper;
import com.common.network.rx.RxUtil;
import com.common.utils.FileUtil;
import com.common.utils.ToastUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

public class AudioRecordActivity extends AppCompatActivity {

    private final static String TAG = "TestActivity";
    private File mFile;
    private final SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd/HH-mm-ss", Locale.CHINA);
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private AudioRecordProxy mAudioRecordProxy;
    private final List<String> logList = new ArrayList<>();
    private CommonAdapter<String> mCommonAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mAudioRecordProxy = new AudioRecordProxy(this);
        findViewById(R.id.start).setOnClickListener(v -> {
            String filePath = Util.getFilePath(".pcm");
            Log.e(TAG, "onClick: stringBuilder: " + filePath);
            boolean instanceFile = FileUtil.INSTANCE.createFile(filePath);
            if (instanceFile) {
                Log.e(TAG, "onClick: instanceFile: " + true);
                mFile = new File(filePath);
                notifyLogRefresh("??????????????? " + filePath);
                mCompositeDisposable.add(RxUtil.getIoScheduler().scheduleDirect(mAudioRecordProxy.startRecording(mFile)));
                ToastUtil.INSTANCE.showShort(getApplicationContext(), "????????????...");
            }
        });


        findViewById(R.id.end).setOnClickListener(v -> {
            mAudioRecordProxy.stopRecord();
            if (mFile != null) {
                Disposable disposable = RxUtil.getIoScheduler().scheduleDirect(() -> {
                    String absolutePath = mFile.getPath();
                    Log.e(TAG, "onClick: absolutePath: " + absolutePath);
                    String finalPath = absolutePath.replace("pcm", "wav");
                    Log.e(TAG, "onClick: finalPath: " + finalPath);
                    FileUtil.INSTANCE.createFile(finalPath);
                    notifyLogRefresh("??????????????? " + finalPath);
                    Runnable runnable = MultimediaHelper.convertPcm2Wav(mFile, new File(finalPath), mAudioRecordProxy.getSampleRateInHz(), AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
                    runnable.run();
                    notifyLogRefresh("??????wav??????????????????????????? " + absolutePath);
                    boolean delete = mFile.delete();
                    Log.e(TAG, "run: delete: " + delete);
                    ToastUtil.INSTANCE.showShort(getApplicationContext(), "????????????...");
                });
                mCompositeDisposable.add(disposable);
            }
        });

        initRecyclerView();
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.mRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mCommonAdapter = new CommonAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<>()) {
            @Override
            public void convert(@NonNull ViewHolder holder, String o) {
                holder.setText(android.R.id.text1, o);
            }
        };
        recyclerView.setAdapter(mCommonAdapter);
    }

    public void notifyLogRefresh(String log) {
        if (mCommonAdapter != null) {
            mCommonAdapter.addData(log);
            int itemCount = mCommonAdapter.getItemCount();
            RxUtil.getMainScheduler().scheduleDirect(() -> mCommonAdapter.notifyItemInserted(itemCount - 1));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.dispose();
        mAudioRecordProxy.releaseAudioRecord();
    }
}
