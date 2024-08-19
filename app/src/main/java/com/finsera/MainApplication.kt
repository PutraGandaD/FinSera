package com.finsera

import android.app.Application
import com.finsera.common.utils.sharedpref.SharedPreferenceManager
import com.finsera.di.DataModule.dataModule
import com.finsera.di.DataModule.databaseDaoModule
import com.finsera.di.DataModule.databaseInitModule
import com.finsera.di.DataModule.repositoryModule
import com.finsera.di.NetworkModule.networkModule
import com.finsera.di.PresentationModule.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(
                dataModule,
                databaseInitModule,
                databaseDaoModule,
                networkModule,
                repositoryModule,
                viewModelModule,
                useCaseModule)
        }
    }
}