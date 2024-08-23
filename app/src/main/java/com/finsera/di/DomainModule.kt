package com.finsera.di

import com.finsera.domain.model.TransferVa
import com.finsera.domain.usecase.auth.CheckAppPinCreatedUseCase
import com.finsera.domain.usecase.auth.CheckLoggedInUseCase
import com.finsera.domain.usecase.auth.CreateAppPinUseCase
import com.finsera.domain.usecase.auth.GetUserInfoUseCase
import com.finsera.domain.usecase.auth.LoginAppPinUserUseCase
import com.finsera.domain.usecase.auth.LoginUserUseCase
import com.finsera.domain.usecase.infosaldo.InfoSaldoUseCase
import com.finsera.domain.usecase.mutasi.DownloadMutasiUseCase
import com.finsera.domain.usecase.mutasi.MutasiUseCase
import com.finsera.domain.usecase.notifikasi.NotifikasiUseCase
import com.finsera.domain.usecase.qris.QRShareUseCase
import com.finsera.domain.usecase.qris.TransferQrisMerchantUseCase
import com.finsera.domain.usecase.transfer.antar_bank.CariDaftarTersimpanAntarUseCase
import com.finsera.domain.usecase.transfer.antar_bank.CekRekeningAntarUseCase
import com.finsera.domain.usecase.transfer.antar_bank.GetDaftarTersimpanAntarUseCase
import com.finsera.domain.usecase.transfer.antar_bank.GetListBankUseCase
import com.finsera.domain.usecase.transfer.antar_bank.TambahDaftarTersimpanAntarUseCase
import com.finsera.domain.usecase.transfer.antar_bank.TransferAntarBankUseCase
import com.finsera.domain.usecase.transfer.ewallet.CariDaftarEWalletTersimpanUseCase
import com.finsera.domain.usecase.transfer.ewallet.CekEWalletUseCase
import com.finsera.domain.usecase.transfer.ewallet.GetDaftarTersimpanEWalletUseCase
import com.finsera.domain.usecase.transfer.ewallet.TambahDaftarTersimpanEWalletUseCase
import com.finsera.domain.usecase.transfer.ewallet.TransferEWalletUseCase
import com.finsera.domain.usecase.transfer.sesama_bank.CariDaftarTersimpanSesamaUseCase
import com.finsera.domain.usecase.transfer.sesama_bank.CekRekeningSesamaUseCase
import com.finsera.domain.usecase.transfer.sesama_bank.GetDaftarTersimpanSesamaUseCase
import com.finsera.domain.usecase.transfer.sesama_bank.TambahDaftarTersimpanSesamaUseCase
import com.finsera.domain.usecase.transfer.sesama_bank.TransferSesamaBankUseCase
import com.finsera.domain.usecase.transfer.virtual_account.CariDaftarVaTersimpanUseCase
import com.finsera.domain.usecase.transfer.virtual_account.CekVirtualAccountUseCase
import com.finsera.domain.usecase.transfer.virtual_account.GetDaftarTersimpanVaUseCase
import com.finsera.domain.usecase.transfer.virtual_account.TambahDaftarTersimpanVaUseCase
import com.finsera.domain.usecase.transfer.virtual_account.TransferVaUseCase
import org.koin.dsl.module

object DomainModule {
    val useCaseModule = module {
        factory { LoginUserUseCase(get()) }
        factory { CheckLoggedInUseCase(get()) }
        factory { LoginAppPinUserUseCase(get()) }
        factory { InfoSaldoUseCase(get(), get()) }
        factory { GetUserInfoUseCase(get()) }
        factory { MutasiUseCase(get(), get()) }
        factory { TransferSesamaBankUseCase(get(), get()) }
        factory { CekRekeningSesamaUseCase(get(), get()) }
        factory { DownloadMutasiUseCase(get()) }
        factory { GetDaftarTersimpanSesamaUseCase(get()) }
        factory { TambahDaftarTersimpanSesamaUseCase(get()) }
        factory { CariDaftarTersimpanSesamaUseCase(get()) }
        factory { CekRekeningAntarUseCase(get(), get()) }
        factory { CekVirtualAccountUseCase(get()) }
        factory { TransferVaUseCase(get()) }
        factory { CekEWalletUseCase(get()) }
        factory { TransferEWalletUseCase(get()) }
        factory { GetDaftarTersimpanVaUseCase(get()) }
        factory { TambahDaftarTersimpanVaUseCase(get()) }
        factory { CariDaftarVaTersimpanUseCase(get()) }
        factory { GetDaftarTersimpanEWalletUseCase(get()) }
        factory { TambahDaftarTersimpanEWalletUseCase(get()) }
        factory { CariDaftarEWalletTersimpanUseCase(get()) }
        factory { GetListBankUseCase(get()) }
        factory { TransferAntarBankUseCase(get(), get()) }
        factory { GetDaftarTersimpanAntarUseCase(get()) }
        factory { TambahDaftarTersimpanAntarUseCase(get()) }
        factory { CariDaftarTersimpanAntarUseCase(get()) }
        factory { CheckAppPinCreatedUseCase(get()) }
        factory { CreateAppPinUseCase(get()) }
        factory { TransferQrisMerchantUseCase(get(), get()) }
        factory { QRShareUseCase(get(), get()) }
        factory { NotifikasiUseCase(get()) }
    }
}