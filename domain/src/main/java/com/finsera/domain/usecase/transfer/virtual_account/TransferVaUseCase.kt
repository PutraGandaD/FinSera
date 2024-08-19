package com.finsera.domain.usecase.transfer.virtual_account

import com.finsera.common.utils.Resource
import com.finsera.domain.model.CekVa
import com.finsera.domain.repository.ITransferRepository
import java.util.concurrent.Flow

class TransferVaUseCase(private val transferRepository: ITransferRepository) {
    suspend fun invoke(vaAccountNum: String, pin: String) =
        transferRepository.transferVirtualAccount(vaAccountNum, pin)

}