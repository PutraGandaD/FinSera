package com.finsera.domain.usecase.transfer.ewallet

import com.finsera.domain.repository.ITransferRepository

class CekEWalletUseCase(private val transferRepository: ITransferRepository) {
    suspend fun invoke(eWalletId: Int, eWalletAccountNum: String) =
        transferRepository.cekDataEWallet(eWalletId, eWalletAccountNum)
}