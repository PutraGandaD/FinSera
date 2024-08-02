package com.finsera.domain.usecase.mutasi

import com.finsera.common.utils.Resource
import com.finsera.domain.model.Mutasi
import com.finsera.domain.repository.IMutasiRepository
import kotlinx.coroutines.flow.Flow

class MutasiUseCase (private val repository: IMutasiRepository) {
suspend fun invoke(startDate: String?, endDate: String?, page: Int = 1, size: Int = 10) : Flow<Resource<List<Mutasi>>> {
        return repository.getMutasi(startDate, endDate, page, size)
    }
}