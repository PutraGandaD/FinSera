package com.finsera.domain.repository

import com.finsera.domain.model.Login
import com.finsera.domain.model.Relogin

interface IAuthRepository {
    suspend fun login(username: String, password: String): Login
    suspend fun setUserToken(token: String)
    suspend fun getUserToken() : String
    suspend fun relogin(token: String): Relogin
}