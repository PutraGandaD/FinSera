package com.finsera.data.utils

import com.finsera.data.source.remote.response.cek_rekening_sesama_bank.CekRekeningResponse
import com.finsera.data.source.remote.response.info_saldo.InfoSaldoResponse
import com.finsera.data.source.remote.response.login.LoginResponse
import com.finsera.data.source.remote.response.mutasi.MutasiResponse
import com.finsera.data.source.remote.response.relogin.ReloginResponse
import com.finsera.data.source.remote.response.transfer_sesama_bank.TransferSesamaResponse
import com.finsera.domain.model.CekRekening
import com.finsera.domain.model.Login
import com.finsera.domain.model.Mutasi
import com.finsera.domain.model.Relogin
import com.finsera.domain.model.Saldo
import com.finsera.domain.model.TransferSesama

object DataMapper {
    fun loginDataToDomain(response: LoginResponse): Login {
        return Login(
            code = response.code,
            message = response.message,
            accessToken = response.data?.accessToken,
            refreshToken = response.data?.refreshToken,
            status = response.data?.status,
            userId = response.data?.userId
        )
    }

    fun reloginResponseToDomain(response: ReloginResponse) : Relogin {
        return Relogin(
            data = response?.data,
            message = response.message
        )
    }

    fun infoSaldoResponseToDomain(response: InfoSaldoResponse): Saldo {
        return Saldo(
            amount = response.data.amount.amount,
            currency = response.data.amount.currency,
            customerId = response.data.customerId,
            accountNumber = response.data.accountNumber,
            username = response.data.username
        )
    }

    fun mutasiResponseToDomain(response: MutasiResponse): List<Mutasi>? {
        return response.data?.let {
            it.map {
                Mutasi(
                    amount = it.amountTransfer.amount,
                    destinationName = it.destinationNameAccountNumber,
                    noTransaction = it.noTransaction,
                    transactionDate = it.transactionDate,
                    transactionInformation = it.transactionInformation,
                    transactionType = it.transactionsType
                )
            }
        }
    }

    fun cekRekeningResponseToDomain(response: CekRekeningResponse): CekRekening {
        return CekRekening(
            accountnumRecipient = response.data?.accountnumRecipient,
            recipientName = response.data?.nameRecipient,
            message = response.message
        )
    }

    fun transferSesamaResponseToDomain(response: TransferSesamaResponse): TransferSesama {
        return TransferSesama(
            transactionDate = response.data?.transactionDate,
            note = response.data?.note,
            nominal = response.data?.nominal,
            nameRecipient = response.data?.nameRecipient,
            transactionNum = response.data?.transactionNum,
            accountnumRecipient = response.data?.accountnumRecipient,
            message = response.message
        )
    }


}