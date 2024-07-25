package com.finsera.data.source.remote

import com.finsera.data.source.remote.response.login.LoginResponse
import com.google.gson.JsonObject

class RemoteDataSource(private val apiService: ApiService) {
    suspend fun authLoginUser(username: String, password: String) : LoginResponse? {
        val param = JsonObject().apply {
            addProperty("username", username)
            addProperty("password", password)
        }

        return apiService.loginUser(param)
    }
}