package com.finsera.domain.repository

import com.finsera.domain.model.Bank
import com.finsera.domain.model.CekRekening
import com.finsera.domain.model.TransferSesama

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
}