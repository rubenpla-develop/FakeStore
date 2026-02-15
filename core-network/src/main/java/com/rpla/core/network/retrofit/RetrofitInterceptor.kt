package com.rpla.core.network.retrofit

import okhttp3.Interceptor
import okhttp3.Response

class RetrofitInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        val url = chain.request().url

        requestBuilder.url(url)
        val response = chain.proceed(requestBuilder.build())

        return response
    }
}
