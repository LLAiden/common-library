package com.common.network.rx;

import android.util.Log;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class BaseObserver<T> implements Observer<T> {

    private final static String TAG = "BaseObserver";
    protected Disposable mDisposable;

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        mDisposable = d;
        Log.e(TAG, "onSubscribe: " + d);
    }

    @Override
    public void onNext(T t) {

    }

    @Override
    public void onError(@NonNull Throwable e) {
        Log.e(TAG, "onError: ", e);
    }

    @Override
    public void onComplete() {
        Log.e(TAG, "onComplete: ");
    }
}
