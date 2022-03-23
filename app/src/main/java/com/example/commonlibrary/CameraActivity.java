package com.example.commonlibrary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageProxy;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;

import com.common.media.MultimediaHelper;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * @author LLAiden
 */
public class CameraActivity extends AppCompatActivity {

    private final static String TAG = "CameraActivity";
    private ProcessCameraProvider mProcessCameraProvider;
    private boolean isRecording = false;
    private Context mContext;
    private volatile ConcurrentLinkedQueue<ImageProxy> mLinkedQueue = new ConcurrentLinkedQueue<>();
    private Thread mCurrentThread;
    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1, Long.MAX_VALUE, TimeUnit.SECONDS, new LinkedBlockingDeque<>(1), new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            String threadName = String.valueOf(thread.hashCode());
            thread.setName(threadName);
            Log.e(TAG, "newThread: threadName: " + threadName);
            return thread;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        mContext = this;
        Executor executorService = ContextCompat.getMainExecutor(mContext);
        findViewById(R.id.recordVideo).setOnClickListener(v -> {
            if (!isRecording) {
                isRecording = true;
                ListenableFuture<ProcessCameraProvider> instance = ProcessCameraProvider.getInstance(mContext);
                instance.addListener(() -> {
                    try {
                        mProcessCameraProvider = instance.get();
                        mProcessCameraProvider.unbindAll();
                        ImageCapture imageCapture = new ImageCapture.Builder().build();
                        CameraSelector defaultBackCamera = CameraSelector.DEFAULT_BACK_CAMERA;
                        openSaveThread();
                        mProcessCameraProvider.bindToLifecycle(CameraActivity.this, defaultBackCamera, imageCapture, getImageAnalysis(executorService));

                    } catch (Exception e) {
                        Log.e(TAG, "run: ", e);
                    }
                }, executorService);
            } else {
                isRecording = false;
            }
        });
    }

    private void openSaveThread() {
        threadPoolExecutor.execute(() -> {
            mCurrentThread = Thread.currentThread();
            Log.e(TAG, "run: " + mCurrentThread.getName());

            File file = new File(Util.getRootPath() + "test.yuv");
            Log.e(TAG, "run: " + file.getAbsolutePath());
            try (FileOutputStream fos = new FileOutputStream(file)) {
                while (isRecording) {
                    if (mLinkedQueue.size() == 0) {
                        LockSupport.park();
                    }
                    ImageProxy imageProxy = mLinkedQueue.poll();
                    if (imageProxy != null) {
                        byte[] bytes = MultimediaHelper.yuv420ToNv12(imageProxy);
                        imageProxy.close();
                        fos.write(bytes);
                    }
                }
                fos.flush();
                Log.e(TAG, "run:写入完成... ");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @NonNull
    private ImageAnalysis getImageAnalysis(Executor executorService) {
        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .build();
        imageAnalysis.setAnalyzer(executorService, image -> {
            mLinkedQueue.offer(image);
            LockSupport.unpark(mCurrentThread);
        });
        return imageAnalysis;
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mProcessCameraProvider != null) {
            mProcessCameraProvider.shutdown();
        }
        isRecording = false;
    }
}