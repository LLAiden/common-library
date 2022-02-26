package com.common.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitFactory {

    private val serviceMap = HashMap<String, Any>()

    fun newInstance(baseUrl: String, okHttpClient: OkHttpClient = OkHttpClientFactory.newOkHttpClient()): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }

    private fun <T : Any> create(service: Class<T>, retrofit: Retrofit): T? {
        val cacheService = serviceMap[service.name]
        if (cacheService == null) {
            val targetService = retrofit.create(service)
            serviceMap[service.name] = targetService
            return targetService
        } else {
            if (cacheService.javaClass != service.javaClass) {
                serviceMap.remove(service.name)
                return create(service, retrofit)
            }
        }
        return null
    }
}