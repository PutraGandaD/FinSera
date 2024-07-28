package com.finsera.data.implementation.auth

import com.finsera.data.source.local.LocalDataSource
import com.finsera.data.source.remote.RemoteDataSource
import com.finsera.data.utils.DataMapper
import com.finsera.domain.model.Login
import com.finsera.domain.model.Relogin
import com.finsera.domain.repository.IAuthRepository

class           AuthRepositoryImpl(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) : IAuthRepository {
    override suspend fun login(username: String, password: String): Login {
        val response = remoteDataSource.authLoginUser(username, password)
        return DataMapper.loginDataToDomain(response)
    }

    override suspend fun setUserToken(token: String) {
        localDataSource.setUserToken(token)
    }

    override suspend fun getUserToken(): String {
        return localDataSource.getUserToken()
    }

    override suspend fun relogin(token: String): Relogin {
        TODO("Not yet implemented")
    }
}