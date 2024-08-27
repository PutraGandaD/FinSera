package com.finsera.domain.usecase.mutasi

import com.finsera.common.utils.Resource
import com.finsera.domain.repository.IMutasiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

import okhttp3.ResponseBody
import java.io.IOException

class DownloadMutasiUseCase(private val repository: IMutasiRepository){

    suspend operator fun invoke(startDate: String, endDate: String) : Flow<Resource<ResponseBody>> = flow{
        emit(Resource.Loading())
        try {
            val response = repository.downloadMutasiFile(startDate, endDate)
            emit(Resource.Success(response))
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    emit(Resource.Error("Network error: ${t.message}"))
                }
                else -> {
                    emit(Resource.Error("An unexpected error occurred"))
                }
            }
        }
    }

}