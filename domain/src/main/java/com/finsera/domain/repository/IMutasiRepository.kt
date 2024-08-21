package com.finsera.domain.repository

import com.finsera.common.utils.Resource
import com.finsera.domain.model.Mutasi
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody

interface IMutasiRepository {
    suspend fun getMutasi(startDate: String?, endDate: String?, page: Int) : List<Mutasi>?
    suspend fun downloadMutasiFile(startDate: String, endDate: String): ResponseBody
}
