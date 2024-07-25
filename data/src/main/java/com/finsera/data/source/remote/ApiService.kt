package com.finsera.data.source.remote

import com.finsera.data.source.remote.response.login.LoginResponse
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("auth/user/login")
    suspend fun loginUser(
        @Body raw: JsonObject
    ): LoginResponse
}