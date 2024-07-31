package com.finsera.common.utils.network

import com.finsera.common.utils.Constant.Companion.USER_TOKEN_KEY
import com.finsera.common.utils.sharedpref.SharedPreferenceManager
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.HttpException
import java.io.IOException

class AuthInterceptor() : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = SharedPreferenceManager.getString(USER_TOKEN_KEY, "")

        val original = chain.request()
        val reqBuilder = original.newBuilder()
            .header("Authorization", "Bearer $token")
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")

        return chain.proceed(reqBuilder.build())
    }
}