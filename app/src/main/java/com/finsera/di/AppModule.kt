package com.finsera.di

import com.finsera.data.implementation.auth.AuthRepositoryImpl
import com.finsera.data.source.remote.RemoteDataSource
import com.finsera.domain.repository.IAuthRepository
import com.finsera.domain.usecase.auth.LoginUserUseCase
import com.finsera.ui.fragments.auth.viewmodels.AuthViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object AppModule {
    val appModule = module {
        single { RemoteDataSource(get()) }
    }

    val repositoryModule = module {
        factory <IAuthRepository> { AuthRepositoryImpl(get()) }
    }

    val viewModelModule = module {
        viewModel { AuthViewModel(get()) }
    }

    val useCaseModule = module {
        factory { LoginUserUseCase(get()) }
    }
}