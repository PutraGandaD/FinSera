package com.finsera.di

import com.finsera.domain.model.TransferVa
import com.finsera.domain.usecase.auth.CheckLoggedInUseCase
import com.finsera.domain.usecase.auth.LoginPinUserUseCase
import com.finsera.domain.usecase.auth.LoginUserUseCase
import com.finsera.domain.usecase.infosaldo.InfoSaldoUseCase
import com.finsera.domain.usecase.mutasi.DownloadMutasiUseCase
import com.finsera.domain.usecase.mutasi.MutasiUseCase
import com.finsera.domain.usecase.transfer.sesama_bank.CariDaftarTersimpanSesamaUseCase
import com.finsera.domain.usecase.transfer.sesama_bank.CekRekeningSesamaUseCase
import com.finsera.domain.usecase.transfer.sesama_bank.GetDaftarTersimpanSesamaUseCase
import com.finsera.domain.usecase.transfer.sesama_bank.TambahDaftarTersimpanSesamaUseCase
import com.finsera.domain.usecase.transfer.sesama_bank.TransferSesamaBankUseCase
import com.finsera.domain.usecase.transfer.virtual_account.CekVirtualAccountUseCase
import com.finsera.domain.usecase.transfer.virtual_account.TransferVaUseCase
import org.koin.dsl.module

object DomainModule {
    val useCaseModule = module {
        factory { LoginUserUseCase(get()) }
        factory { CheckLoggedInUseCase(get()) }
        factory { LoginPinUserUseCase(get()) }
        factory { InfoSaldoUseCase(get()) }
        factory { MutasiUseCase(get(), get()) }
        factory { TransferSesamaBankUseCase(get(), get()) }
        factory { CekRekeningSesamaUseCase(get(), get()) }
        factory { DownloadMutasiUseCase(get()) }
        factory { CekVirtualAccountUseCase(get()) }
        factory { TransferVaUseCase(get()) }

        factory { GetDaftarTersimpanSesamaUseCase(get()) }
        factory { TambahDaftarTersimpanSesamaUseCase(get()) }
        factory { CariDaftarTersimpanSesamaUseCase(get()) }
    }
}