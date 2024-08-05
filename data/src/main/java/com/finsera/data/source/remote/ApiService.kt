package com.finsera.data.source.remote

import com.finsera.data.source.remote.response.info_saldo.InfoSaldoResponse
import com.finsera.data.source.remote.response.login.LoginResponse
import com.finsera.data.source.remote.response.mutasi.MutasiResponse
import com.finsera.data.source.remote.response.refresh_token.RefreshTokenResponse
import com.finsera.data.source.remote.response.relogin.ReloginResponse
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("auth/user/login")
    suspend fun loginUser(
        @Body raw: JsonObject
    ): LoginResponse

    @POST("auth/relogin")
    suspend fun reloginUser(
        @Header("Authorization") accessToken: String,
        @Body raw: JsonObject
    ): ReloginResponse

    @POST("auth/user/refresh-token")
    suspend fun refreshToken(
        @Body raw: JsonObject
    ) : RefreshTokenResponse

    @GET("amount")
    suspend fun getSaldo(
        @Header("Authorization") accessToken: String
    ): InfoSaldoResponse

    @GET("mutasi")
    suspend fun getMutasi(
        @Header("Authorization") accessToken: String,
        @Query("startDate") startDate: String?,
        @Query("endDate") endDate: String?,
    ): MutasiResponse

}