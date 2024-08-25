package com.finsera.domain.repository

import com.finsera.data.source.remote.ApiService
import com.finsera.data.source.remote.response.login.LoginResponse
import com.google.gson.JsonObject

class FakeRemoteDataSource(private val apiService: ApiService) {
    suspend fun authLoginUser(username: String, password: String): LoginResponse {
        val param = JsonObject().apply {
            addProperty("username", username)
            addProperty("password", password)
        }

        return apiService.loginUser(param)
    }
}