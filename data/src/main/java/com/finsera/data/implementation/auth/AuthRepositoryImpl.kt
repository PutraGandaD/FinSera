package com.finsera.data.implementation.auth

import com.finsera.data.source.remote.RemoteDataSource
import com.finsera.data.utils.DataMapper
import com.finsera.domain.model.Login
import com.finsera.domain.model.Relogin
import com.finsera.domain.repository.IAuthRepository

class AuthRepositoryImpl(private val remoteDataSource: RemoteDataSource) : IAuthRepository {
    override suspend fun login(username: String, password: String): Login? {
        return remoteDataSource.authLoginUser(username, password)?.data?.let {
            DataMapper.loginDataToDomain(
                it
            )
        }
    }

    override suspend fun relogin(token: String): Relogin {
        TODO("Not yet implemented")
    }
}