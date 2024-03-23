package com.example.haksamo.retrofit

import android.app.Application
import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.google.android.gms.common.internal.service.Common
import okhttp3.Interceptor
import okhttp3.Response

// 모든 api 요청에 자동으로 토큰을 셋팅하는 클래스
class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            //.addHeader("Authorzation", App.prefs.token ?: "")
            .build()

        return chain.proceed(request)
    }
}