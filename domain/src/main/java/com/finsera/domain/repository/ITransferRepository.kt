package com.finsera.domain.repository

import com.finsera.domain.model.Bank
import com.finsera.common.utils.Resource
import com.finsera.domain.model.CekRekening
import com.finsera.domain.model.CekVa
import com.finsera.domain.model.TransferSesama
import com.finsera.domain.model.TransferVa
import kotlinx.coroutines.flow.Flow

interface ITransferRepository {
    suspend fun cekDataRekeningSesama(
        accountNumRecipient: String,
    ): CekRekening

    suspend fun transferSesamaBank(
        accountNumRecipient: String,
        nominal: Double,
        note: String,
        pin: String
    ): TransferSesama

    suspend fun getListBank() : List<Bank>
    suspend fun cekDataVirtualAccount(
        vaAccountNum: String
    ): Flow<Resource<CekVa>>

    suspend fun transferVirtualAccount(
        vaAccountNum: String,
        pin: String
    ): Flow<Resource<TransferVa>>

}