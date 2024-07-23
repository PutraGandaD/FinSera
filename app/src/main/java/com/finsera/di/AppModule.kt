package com.finsera.di

import android.content.Context
import com.finsera.common.utils.sharedpref.SharedPreferenceManager
import org.koin.dsl.module

object AppModule {
    val appModule = module {
        single { initSharedPreferencesManager(get()) }
    }

    private fun initSharedPreferencesManager(context: Context) {
        SharedPreferenceManager.init(context)
    }
}