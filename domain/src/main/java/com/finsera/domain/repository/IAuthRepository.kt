package com.finsera.domain.repository

import com.finsera.domain.model.Login
import com.finsera.domain.model.Profiling
import com.finsera.domain.model.Relogin

interface IAuthRepository {
    suspend fun login(username: String, password: String): Login
    suspend fun relogin(mpin: String): Relogin
    suspend fun refreshAccessToken(refreshToken: String)
    fun setAccessToken(accessToken: String)
    fun getAccessToken() : String
    fun setRefreshToken(refreshToken: String)
    fun getRefreshToken() : String
    fun setLoginStatus(status: Boolean)
    fun getLoginStatus() : Boolean
    fun setAppLockPin(newPin: String)
    fun getAppLockPin() : String
    fun saveUsernameForRecovery(username: String)
    fun getUsernameForRecovery() : String
    fun saveUserInfo(name: String, accountNum: String)
    fun getUserInfo() : Pair<String, String>
    suspend fun getUserProfiling(accessToken: String) : Profiling
    fun clearSharedPreferences()
}