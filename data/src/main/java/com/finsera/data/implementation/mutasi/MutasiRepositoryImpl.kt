package com.finsera.data.implementation.mutasi

import com.finsera.data.source.local.LocalDataSource
import com.finsera.data.source.remote.RemoteDataSource
import com.finsera.data.utils.DataMapper
import com.finsera.domain.model.Mutasi
import com.finsera.domain.repository.IMutasiRepository

class MutasiRepositoryImpl(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
): IMutasiRepository {
    override suspend fun getMutasi(
        startDate: String?,
        endDate: String?,
    ): List<Mutasi>? {
        val accessToken = localDataSource.getAccessToken()
        val response = remoteDataSource.getMutasi(accessToken,startDate, endDate)
        val data = DataMapper.mutasiResponseToDomain(response)
        return data
    }
}