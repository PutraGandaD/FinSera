package com.finsera.data.implementation.mutasi

import com.finsera.data.source.local.LocalDataSource
import com.finsera.data.source.remote.RemoteDataSource
import com.finsera.data.utils.DataMapper
import com.finsera.domain.model.Mutasi
import com.finsera.domain.repository.IMutasiRepository
import okhttp3.ResponseBody

class MutasiRepositoryImpl(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
): IMutasiRepository {
    override suspend fun getMutasi(
        startDate: String?,
        endDate: String?,
        page: Int
    ): List<Mutasi>? {
        val accessToken = localDataSource.getAccessToken()
        val response = remoteDataSource.getMutasi(accessToken,startDate, endDate, page)
        val data = DataMapper.mutasiResponseToDomain(response)
        return data
    }

    override suspend fun downloadMutasiFile(startDate: String, endDate: String): ResponseBody {
        val accessToken = localDataSource.getAccessToken()
        return remoteDataSource.downloadMutasi(accessToken, startDate, endDate)
    }
}