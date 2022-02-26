package com.common.network

import retrofit2.Retrofit

object NetworkManager {

    private val retrofit: Retrofit

    init {
        val newOkHttpClient = OkHttpClientFactory.newOkHttpClient()
        retrofit = RetrofitFactory.newInstance("https://www.wanandroid.com", newOkHttpClient)
    }

    fun <T> create(service: Class<T>): T {
        return retrofit.create(service)
    }
}