package com.finsera.domain.repository.notifikasi

import com.finsera.common.utils.Resource
import com.finsera.data.utils.DataMapper
import com.finsera.domain.model.Notifikasi
import com.finsera.domain.repository.FakeRemoteDataSource
import com.finsera.domain.repository.INotifkasiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FakeNotifikasiRepository (
    private val fakeRemoteDataSource: FakeRemoteDataSource
):INotifkasiRepository{
    override suspend fun getNotifikasi(): Flow<Resource<List<Notifikasi>>> {
        val accessToken = "token"
        return fakeRemoteDataSource.getNotifikasi(accessToken).map {
            when(it){
                is Resource.Success -> Resource.Success(
                    DataMapper.notifResponseToDomain(it.data!!)
                )
                is Resource.Error->Resource.Error(it.message.toString())
                is Resource.Loading->Resource.Loading()
            }
        }
    }
}