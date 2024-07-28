package com.finsera.di

import com.finsera.common.utils.sharedpref.SharedPreferenceManager
import com.finsera.data.implementation.auth.AuthRepositoryImpl
import com.finsera.data.source.local.LocalDataSource
import com.finsera.data.source.remote.RemoteDataSource
import com.finsera.domain.repository.IAuthRepository
import com.finsera.domain.usecase.auth.LoginUserUseCase
import com.finsera.ui.fragments.auth.viewmodels.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object AppModule {
    val appModule = module {
        single { LocalDataSource(get()) }
        single { RemoteDataSource(get()) }
        single {
            SharedPreferenceManager.init(get())
            SharedPreferenceManager
        }
    }

    val repositoryModule = module {
        factory <IAuthRepository> { AuthRepositoryImpl(get(), get()) }
    }

    val viewModelModule = module {
        viewModel { LoginViewModel(get(), get()) }
    }

    val useCaseModule = module {
        factory { LoginUserUseCase(get()) }
    }
}