package com.finsera.domain.repository

import com.finsera.domain.model.Bank
import com.finsera.common.utils.Resource
import com.finsera.domain.model.CekEWallet
import com.finsera.domain.model.CekRekening
import com.finsera.domain.model.TransferAntar
import com.finsera.domain.model.CekVa
import com.finsera.domain.model.QRShare
import com.finsera.domain.model.TransferQrisMerchant
import com.finsera.domain.model.TransferEWallet
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

    suspend fun cekDataRekeningAntar(
        idBank: Int,
        accountNumRecipient: String
    ) : CekRekening

    suspend fun transferAntarBank(
        idBank: Int,
        accountNumRecipient: String,
        nominal: Double,
        note: String,
        pin: String
    ): TransferAntar

    suspend fun cekDataVirtualAccount(
        vaAccountNum: String
    ): Flow<Resource<CekVa>>

    suspend fun transferVirtualAccount(
        vaAccountNum: String,
        pin: String
    ): Flow<Resource<TransferVa>>

    suspend fun transferQrisMerchant(
        merchantNo: String,
        merchantName: String,
        nominal: Double,
        pin: String
    ) : TransferQrisMerchant

    suspend fun cekDataEWallet(
        eWalletId: Int,
        eWalletAccountNum: String
    ):Flow<Resource<CekEWallet>>

    suspend fun transferEWallet(
        eWalletId: Int,
        eWalletAccountNum: String,
        nominal: Double,
        note: String,
        pin: String
    ): Flow<Resource<TransferEWallet>>

    suspend fun shareQR() : QRShare
}