package com.finsera.data.implementation.notifikasi

import com.finsera.common.utils.Resource
import com.finsera.data.source.local.LocalDataSource
import com.finsera.data.source.remote.RemoteDataSource
import com.finsera.data.utils.DataMapper
import com.finsera.domain.model.Notifikasi
import com.finsera.domain.repository.INotifkasiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NotifikasiRepositoryImpl(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) :INotifkasiRepository{
    override suspend fun getNotifikasi(): Flow<Resource<List<Notifikasi>>> {
        val accessToken = localDataSource.getAccessToken()
        return remoteDataSource.getNotifikasi(accessToken).map {
            when(it){
                is Resource.Success -> Resource.Success(
                    DataMapper.notifResponseToDomain(it.data!!)
                )
                is Resource.Error -> Resource.Error(it.message.toString())
                is Resource.Loading -> Resource.Loading()
            }
        }
    }
}