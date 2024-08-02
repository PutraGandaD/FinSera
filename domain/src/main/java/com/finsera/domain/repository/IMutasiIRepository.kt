package com.finsera.domain.repository

import com.finsera.common.utils.Resource
import com.finsera.domain.model.Mutasi
import kotlinx.coroutines.flow.Flow

interface IMutasiRepository {
    suspend fun getMutasi(startDate: String?, endDate: String?, page: Int = 1, size: Int = 10) : Flow<Resource<List<Mutasi>>>
}
