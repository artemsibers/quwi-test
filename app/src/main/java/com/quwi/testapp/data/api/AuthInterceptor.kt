package com.quwi.testapp.data.api

import okhttp3.Interceptor
import okhttp3.Response

object AuthInterceptor: Interceptor {
    private var token: String? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBuilder = request.newBuilder()
        token?.let {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }
        return chain.proceed(requestBuilder.build())
    }
    fun setToken(token: String?) {
        this.token = token
    }
}