package com.finsera.domain.repository

import com.finsera.data.utils.DataMapper
import com.finsera.domain.model.Login
import com.finsera.domain.model.Profiling
import com.finsera.domain.model.Relogin
import com.finsera.domain.utils.Mapper

class FakeAuthRepository(
    private val fakeRemoteDataSource: FakeRemoteDataSource
) : IAuthRepository {
    override suspend fun login(username: String, password: String): Login {
        val response = fakeRemoteDataSource.authLoginUser(username, password)

        return Mapper.loginDataToDomain(response)
    }

    override suspend fun relogin(mpin: String): Relogin {
        TODO("Not yet implemented")
    }

    override suspend fun refreshAccessToken(refreshToken: String) {
        TODO("Not yet implemented")
    }

    override fun setAccessToken(accessToken: String) {
        TODO("Not yet implemented")
    }

    override fun getAccessToken(): String {
        TODO("Not yet implemented")
    }

    override fun setRefreshToken(refreshToken: String) {
        TODO("Not yet implemented")
    }

    override fun getRefreshToken(): String {
        TODO("Not yet implemented")
    }

    override fun setLoginStatus(status: Boolean) {
        TODO("Not yet implemented")
    }

    override fun getLoginStatus(): Boolean {
        TODO("Not yet implemented")
    }

    override fun setAppLockPin(newPin: String) {
        TODO("Not yet implemented")
    }

    override fun getAppLockPin(): String {
        TODO("Not yet implemented")
    }

    override fun saveUsernameForRecovery(username: String) {
        TODO("Not yet implemented")
    }

    override fun getUsernameForRecovery(): String {
        TODO("Not yet implemented")
    }

    override fun saveUserInfo(name: String, accountNum: String) {
        TODO("Not yet implemented")
    }

    override fun getUserInfo(): Pair<String, String> {
        TODO("Not yet implemented")
    }

    override suspend fun getUserProfiling(accessToken: String): Profiling {
        TODO("Not yet implemented")
    }

    override fun clearSharedPreferences() {
        TODO("Not yet implemented")
    }
}