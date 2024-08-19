package com.finsera.data.implementation.transfer

import com.finsera.data.source.local.LocalDataSource
import com.finsera.data.source.remote.RemoteDataSource
import com.finsera.data.utils.DataMapper
import com.finsera.domain.model.Bank
import com.finsera.domain.model.CekRekening
import com.finsera.domain.model.TransferAntar
import com.finsera.domain.model.TransferSesama
import com.finsera.domain.repository.ITransferRepository

class TransferRepositoryImpl(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) : ITransferRepository {
    override suspend fun cekDataRekeningSesama(accountNumRecipient: String): CekRekening {
        val accessToken = localDataSource.getAccessToken()
        val request = remoteDataSource.cekRekeningSesamaBank(accessToken, accountNumRecipient)

        return DataMapper.cekRekeningSesamaResponseToDomain(request)
    }

    override suspend fun transferSesamaBank(
        accountNumRecipient: String,
        nominal: Double,
        note: String,
        pin: String
    ): TransferSesama {
        val accessToken = localDataSource.getAccessToken()
        val request = remoteDataSource.transferSesamaBank(
            accessToken,
            accountNumRecipient,
            nominal,
            note,
            pin
        )
        return DataMapper.transferSesamaResponseToDomain(request)
    }

    override suspend fun getListBank(): List<Bank> {
        val accessToken = localDataSource.getAccessToken()
        val request = remoteDataSource.getListBank(accessToken)

        return DataMapper.listBankToDomain(request)
    }

    override suspend fun cekDataRekeningAntar(
        idBank: Int,
        accountNumRecipient: String
    ): CekRekening {
        val accessToken = localDataSource.getAccessToken()
        val request = remoteDataSource.cekRekeningAntarBank(accessToken, idBank, accountNumRecipient)

        return DataMapper.cekRekeningAntarResponseToDomain(request)
    }

    override suspend fun transferAntarBank(
        idBank: Int,
        accountNumRecipient: String,
        nominal: Double,
        note: String,
        pin: String
    ): TransferAntar {
        //TODO("Not yet implemented")
        val accessToken = localDataSource.getAccessToken()
        val request = remoteDataSource.transferAntarBank(
            accessToken,
            idBank,
            accountNumRecipient,
            nominal,
            note,
            pin
        )

        return DataMapper.transferAntarResponseToDomain(request)
    }
}