package com.finsera.di

import androidx.room.Room
import com.finsera.common.utils.sharedpref.SharedPreferenceManager
import com.finsera.data.implementation.auth.AuthRepositoryImpl
import com.finsera.data.implementation.infosaldo.SaldoRepositoryImpl
import com.finsera.data.implementation.mutasi.MutasiRepositoryImpl
import com.finsera.data.implementation.transfer.TransferRepositoryImpl
import com.finsera.data.source.local.LocalDataSource
import com.finsera.data.source.local.dao.daftar_tersimpan.transfer_antar.TransferAntarTersimpanDao
import com.finsera.data.source.local.dao.daftar_tersimpan.transfer_sesama.TransferSesamaTersimpanDao
import com.finsera.data.source.local.db.daftar_tersimpan.DaftarTersimpanDatabase
import com.finsera.data.source.remote.RemoteDataSource
import com.finsera.domain.repository.IAuthRepository
import com.finsera.domain.repository.IMutasiRepository
import com.finsera.domain.repository.ISaldoRepository
import com.finsera.domain.repository.ITransferRepository
import org.koin.dsl.module

object DataModule {
    val dataModule = module {
        single { LocalDataSource(get()) }
        single { RemoteDataSource(get()) }
        single {
            SharedPreferenceManager.init(get())
            SharedPreferenceManager
        }
    }

    val databaseInitModule = module {
        single {
            Room.databaseBuilder(
                get(),
                DaftarTersimpanDatabase::class.java,
                "daftar_tersimpan_database"
            ).build()
        }
    }

    val databaseDaoModule = module {
        single<TransferSesamaTersimpanDao> {
            val database = get<DaftarTersimpanDatabase>()
            database.transferSesamaTersimpanDao()
        }

        single<TransferAntarTersimpanDao> {
            val database = get<DaftarTersimpanDatabase>()
            database.transferAntarTersimpanDao()
        }
    }

    val repositoryModule = module {
        factory<IAuthRepository> { AuthRepositoryImpl(get(), get()) }
        factory<ISaldoRepository> { SaldoRepositoryImpl(get(), get()) }
        factory<IMutasiRepository>{ MutasiRepositoryImpl(get(), get()) }
        factory<ITransferRepository> { TransferRepositoryImpl(get(), get()) }
    }
}