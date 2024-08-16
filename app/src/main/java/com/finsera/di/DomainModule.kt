package com.finsera.di

import com.finsera.domain.usecase.auth.CheckAppPinCreatedUseCase
import com.finsera.domain.usecase.auth.CheckLoggedInUseCase
import com.finsera.domain.usecase.auth.CreateAppPinUseCase
import com.finsera.domain.usecase.auth.LoginAppPinUserUseCase
import com.finsera.domain.usecase.auth.LoginUserUseCase
import com.finsera.domain.usecase.infosaldo.InfoSaldoUseCase
import com.finsera.domain.usecase.mutasi.DownloadMutasiUseCase
import com.finsera.domain.usecase.mutasi.MutasiUseCase
import com.finsera.domain.usecase.transfer.antar_bank.GetListBankUseCase
import com.finsera.domain.usecase.transfer.sesama_bank.CariDaftarTersimpanSesamaUseCase
import com.finsera.domain.usecase.transfer.sesama_bank.CekRekeningSesamaUseCase
import com.finsera.domain.usecase.transfer.sesama_bank.GetDaftarTersimpanSesamaUseCase
import com.finsera.domain.usecase.transfer.sesama_bank.TambahDaftarTersimpanSesamaUseCase
import com.finsera.domain.usecase.transfer.sesama_bank.TransferSesamaBankUseCase
import org.koin.dsl.module

object DomainModule {
    val useCaseModule = module {
        factory { LoginUserUseCase(get()) }
        factory { CheckLoggedInUseCase(get()) }
        factory { LoginAppPinUserUseCase(get()) }
        factory { InfoSaldoUseCase(get()) }
        factory { MutasiUseCase(get(), get()) }
        factory { TransferSesamaBankUseCase(get(), get()) }
        factory { CekRekeningSesamaUseCase(get(), get()) }
        factory { DownloadMutasiUseCase(get()) }

        factory { GetDaftarTersimpanSesamaUseCase(get()) }
        factory { TambahDaftarTersimpanSesamaUseCase(get()) }
        factory { CariDaftarTersimpanSesamaUseCase(get()) }
        factory { GetListBankUseCase(get()) }

        factory { CheckAppPinCreatedUseCase(get()) }
        factory { CreateAppPinUseCase(get()) }
    }
}