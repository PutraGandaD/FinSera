package com.finsera.common.utils.network

import com.finsera.common.utils.Constant.Companion.USER_TOKEN_KEY
import com.finsera.common.utils.sharedpref.SharedPreferenceManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = SharedPreferenceManager.getString(USER_TOKEN_KEY, "")

        val original = chain.request()
        val reqBuilder = original.newBuilder()
            .header("Authorization", "Bearer $token")
            .header("Accept", "application/json")
        val request = reqBuilder.build()

        return chain.proceed(request)
    }
}