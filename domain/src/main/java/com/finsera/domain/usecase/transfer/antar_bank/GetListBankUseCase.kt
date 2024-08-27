package com.finsera.domain.usecase.transfer.antar_bank

import com.finsera.common.utils.Resource
import com.finsera.domain.model.Bank
import com.finsera.domain.repository.ITransferRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetListBankUseCase(
    private val repository: ITransferRepository
) {
    suspend operator fun invoke() : Flow<Resource<List<Bank>>> = flow {
        emit(Resource.Loading())
        try {
            val response = repository.getListBank()
            emit(Resource.Success(response))
        } catch (t: Throwable) {
            emit(Resource.Error(t.localizedMessage))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage))
        }
    }
}