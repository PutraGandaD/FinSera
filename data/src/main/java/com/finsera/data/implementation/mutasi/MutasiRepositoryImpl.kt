package com.finsera.data.implementation.mutasi

import com.finsera.common.utils.Resource
import com.finsera.data.source.local.LocalDataSource
import com.finsera.data.source.remote.RemoteDataSource
import com.finsera.data.utils.DataMapper
import com.finsera.domain.model.Mutasi
import com.finsera.domain.repository.IMutasiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MutasiRepositoryImpl(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
): IMutasiRepository {
    override suspend fun getMutasi(
        startDate: String?,
        endDate: String?,
        page: Int,
        size: Int
    ): Flow<Resource<List<Mutasi>>> {
        return flow{
            emit(Resource.Loading())
            try {
                val accessToken = localDataSource.getAccessToken()
                val response = remoteDataSource.getMutasi(accessToken,startDate, endDate, page, size)
                val data = DataMapper.mutasiResponseToDomain(response)
                emit(Resource.Success(data))
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
            }
        }
    }
}