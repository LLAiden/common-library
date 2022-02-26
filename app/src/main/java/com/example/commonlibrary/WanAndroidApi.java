package com.example.commonlibrary;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;

public interface WanAndroidApi {

    @GET("wxarticle/chapters/json")
    Observable<WeChatPublicAccountBean> getWeChatPublicAccount();

}
