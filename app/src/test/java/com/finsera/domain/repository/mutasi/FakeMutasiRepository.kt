package com.finsera.domain.repository.mutasi

import com.finsera.data.utils.DataMapper
import com.finsera.domain.model.Mutasi
import com.finsera.domain.repository.FakeRemoteDataSource
import com.finsera.domain.repository.IMutasiRepository
import okhttp3.ResponseBody

class FakeMutasiRepository(
    private val fakeRemoteDataSource: FakeRemoteDataSource
) :IMutasiRepository{
    override suspend fun getMutasi(startDate: String?, endDate: String?, page: Int): List<Mutasi>? {
        val accessToken = "token"
        val response =fakeRemoteDataSource.getMutasi(accessToken,startDate, endDate, page)
        val data = DataMapper.mutasiResponseToDomain(response)
        return data
    }

    override suspend fun downloadMutasiFile(startDate: String, endDate: String): ResponseBody {
        TODO("Not yet implemented")
    }
}