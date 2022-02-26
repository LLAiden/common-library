package com.common.network.rx;

import io.reactivex.rxjava3.disposables.Disposable;

public abstract class SimpleObserver<T> extends BaseObserver<T> {

    @Override
    public void onNext(T t) {
        oNext(t, mDisposable);
    }

    public abstract void oNext(T t, Disposable disposable);

}
