package com.finsera.di

import com.finsera.common.utils.sharedpref.SharedPreferenceManager
import com.finsera.data.implementation.auth.AuthRepositoryImpl
import com.finsera.data.implementation.infosaldo.SaldoRepositoryImpl
import com.finsera.data.implementation.mutasi.MutasiRepositoryImpl
import com.finsera.data.implementation.transfer.TransferRepositoryImpl
import com.finsera.data.source.local.LocalDataSource
import com.finsera.data.source.remote.RemoteDataSource
import com.finsera.domain.repository.IAuthRepository
import com.finsera.domain.repository.IMutasiRepository
import com.finsera.domain.repository.ISaldoRepository
import com.finsera.domain.repository.ITransferRepository
import com.finsera.domain.usecase.auth.CheckLoggedInUseCase
import com.finsera.domain.usecase.auth.LoginPinUserUseCase
import com.finsera.domain.usecase.auth.LoginUserUseCase
import com.finsera.domain.usecase.infosaldo.InfoSaldoUseCase
import com.finsera.domain.usecase.mutasi.MutasiUseCase
import com.finsera.domain.usecase.transfer_sesama.CekRekeningSesamaUseCase
import com.finsera.domain.usecase.transfer_sesama.TransferSesamaBankUseCase
import com.finsera.ui.fragments.auth.viewmodels.LoginPinViewModel
import com.finsera.ui.fragments.auth.viewmodels.LoginViewModel
import com.finsera.ui.fragments.home.viewmodel.HomeViewModel
import com.finsera.ui.fragments.info.mutasi.viewmodel.MutasiViewModel
import com.finsera.ui.fragments.info.saldo.viewmodel.InfoSaldoViewModel
import com.finsera.ui.fragments.transfer.sesama_bank.viewmodel.CekRekeningSesamaViewModel
import com.finsera.ui.fragments.transfer.sesama_bank.viewmodel.TransferSesamaBankViewModel
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
        factory<IAuthRepository> { AuthRepositoryImpl(get(), get()) }
        factory<ISaldoRepository> { SaldoRepositoryImpl(get(), get())}
        factory<IMutasiRepository>{ MutasiRepositoryImpl(get(), get()) }
        factory<ITransferRepository> { TransferRepositoryImpl(get(), get()) }
    }

    val viewModelModule = module {
        viewModel { LoginViewModel(get(), get(), get()) }
        viewModel { LoginPinViewModel(get(), get()) }
        viewModel { HomeViewModel(get()) }
        viewModel { InfoSaldoViewModel(get()) }
        viewModel { MutasiViewModel(get(), get()) }
        viewModel { TransferSesamaBankViewModel(get(), get()) }
        viewModel { CekRekeningSesamaViewModel(get(), get()) }
    }

    val useCaseModule = module {
        factory { LoginUserUseCase(get()) }
        factory { CheckLoggedInUseCase(get()) }
        factory { LoginPinUserUseCase(get()) }
        factory { InfoSaldoUseCase(get()) }
        factory { MutasiUseCase(get(), get()) }
        factory { TransferSesamaBankUseCase(get(), get()) }
        factory { CekRekeningSesamaUseCase(get(), get()) }
    }

    val databaseModule = module {

    }
}