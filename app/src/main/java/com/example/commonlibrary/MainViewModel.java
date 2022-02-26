package com.example.commonlibrary;


import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.common.network.NetworkManager;
import com.common.network.rx.RxUtil;
import com.common.network.rx.SimpleObserver;

import io.reactivex.rxjava3.disposables.Disposable;

public class MainViewModel extends ViewModel {

    private final MutableLiveData<WeChatPublicAccountBean> weChatPublicAccountBeanMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<WeChatPublicAccountBean> getWeChatPublicAccountBeanMutableLiveData() {
        return weChatPublicAccountBeanMutableLiveData;
    }

    public void getWeChatPublicAccount(@NonNull LifecycleOwner lifecycleOwner) {
        NetworkManager.INSTANCE.create(WanAndroidApi.class)
                .getWeChatPublicAccount()
                .compose(RxUtil.io2main())
                .to(RxUtil.bindLifecycle(lifecycleOwner))
                .subscribe(new SimpleObserver<WeChatPublicAccountBean>() {
                    @Override
                    public void oNext(WeChatPublicAccountBean weChatPublicAccount, Disposable disposable) {
                        getWeChatPublicAccountBeanMutableLiveData().setValue(weChatPublicAccount);
                    }
                });
    }
}
