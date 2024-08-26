package com.finsera.domain.repository.transfer

import com.finsera.common.utils.Resource
import com.finsera.data.utils.DataMapper
import com.finsera.domain.model.Bank
import com.finsera.domain.model.CekEWallet
import com.finsera.domain.model.CekRekening
import com.finsera.domain.model.CekVa
import com.finsera.domain.model.QRShare
import com.finsera.domain.model.TransferAntar
import com.finsera.domain.model.TransferEWallet
import com.finsera.domain.model.TransferQrisMerchant
import com.finsera.domain.model.TransferSesama
import com.finsera.domain.model.TransferVa
import com.finsera.domain.repository.FakeRemoteDataSource
import com.finsera.domain.repository.ITransferRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FakeTransferRepository(
    private val fakeRemoteDataSource: FakeRemoteDataSource
): ITransferRepository {


    override suspend fun cekDataEWallet(
        eWalletId: Int,
        eWalletAccountNum: String
    ): Flow<Resource<CekEWallet>> {
        val accessToken = "token"
        return fakeRemoteDataSource.cekEWallet(accessToken,eWalletId,eWalletAccountNum).map {
            when(it){
                is Resource.Success -> Resource.Success(
                    DataMapper.cekEWalletResponseToDomain(it.data!!)
                )
                is Resource.Error->Resource.Error(it.message.toString())
                is Resource.Loading->Resource.Loading()
            }
        }
    }


    override suspend fun cekDataRekeningSesama(accountNumRecipient: String): CekRekening {
        TODO("Not yet implemented")
    }

    override suspend fun transferSesamaBank(
        accountNumRecipient: String,
        nominal: Double,
        note: String,
        pin: String
    ): TransferSesama {
        TODO("Not yet implemented")
    }

    override suspend fun getListBank(): List<Bank> {
        TODO("Not yet implemented")
    }

    override suspend fun cekDataRekeningAntar(
        idBank: Int,
        accountNumRecipient: String
    ): CekRekening {
        TODO("Not yet implemented")
    }

    override suspend fun transferAntarBank(
        idBank: Int,
        accountNumRecipient: String,
        nominal: Double,
        note: String,
        pin: String
    ): TransferAntar {
        TODO("Not yet implemented")
    }

    override suspend fun cekDataVirtualAccount(vaAccountNum: String): Flow<Resource<CekVa>> {
        TODO("Not yet implemented")
    }

    override suspend fun transferVirtualAccount(
        vaAccountNum: String,
        pin: String
    ): Flow<Resource<TransferVa>> {
        TODO("Not yet implemented")
    }

    override suspend fun transferQrisMerchant(
        merchantNo: String,
        merchantName: String,
        nominal: Double,
        pin: String
    ): TransferQrisMerchant {
        TODO("Not yet implemented")
    }

    override suspend fun transferEWallet(
        eWalletId: Int,
        eWalletAccountNum: String,
        nominal: Double,
        note: String,
        pin: String
    ): Flow<Resource<TransferEWallet>> {
        TODO("Not yet implemented")
    }

    override suspend fun shareQR(): QRShare {
        TODO("Not yet implemented")
    }

}