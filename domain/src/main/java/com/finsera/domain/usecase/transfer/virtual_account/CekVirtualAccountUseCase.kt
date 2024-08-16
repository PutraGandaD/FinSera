package com.finsera.domain.usecase.transfer.virtual_account

import com.finsera.domain.repository.ITransferRepository

class CekVirtualAccountUseCase(private val transferRepository: ITransferRepository) {
    suspend fun invoke(accountNumRecipient: String) =
        transferRepository.cekDataVirtualAccount(accountNumRecipient)

}