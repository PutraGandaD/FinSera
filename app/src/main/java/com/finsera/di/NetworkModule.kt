package com.finsera.di

import android.content.Context
import com.finsera.common.utils.Constant
import com.finsera.common.utils.network.AuthInterceptor
import com.finsera.common.utils.sharedpref.SharedPreferenceManager
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {
    val networkModule = module {
        single { AuthInterceptor() }
    }

    private fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(Constant.BASE_URL).client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }
}