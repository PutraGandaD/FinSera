package com.finsera.domain.usecase.transfer.ewallet

import com.finsera.domain.repository.ITransferRepository

class TransferEWalletUseCase (private val transferRepository: ITransferRepository){
    suspend fun invoke(eWalletId: Int, eWalletAccountNum: String, nominal:Double,note:String,pin: String) =
        transferRepository.transferEWallet(eWalletId, eWalletAccountNum, nominal, note, pin)
}