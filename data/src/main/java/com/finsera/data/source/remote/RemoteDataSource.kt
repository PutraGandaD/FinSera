package com.finsera.data.source.remote

import com.finsera.data.source.remote.response.info_saldo.InfoSaldoResponse
import com.finsera.data.source.remote.response.login.LoginResponse
import com.finsera.data.source.remote.response.mutasi.MutasiResponse
import com.finsera.data.source.remote.response.refresh_token.RefreshTokenResponse
import com.finsera.data.source.remote.response.relogin.ReloginResponse
import com.google.gson.JsonObject

class RemoteDataSource(private val apiService: ApiService) {
    suspend fun authLoginUser(username: String, password: String) : LoginResponse {
        val param = JsonObject().apply {
            addProperty("username", username)
            addProperty("password", password)
        }

        return apiService.loginUser(param)
    }

    suspend fun reloginUser(token: String, mpin: String) : ReloginResponse {
        val param = JsonObject().apply {
            addProperty("mpinAuth", mpin)
        }

        val accessToken = "Bearer $token"

        return apiService.reloginUser(accessToken, param)
    }

    suspend fun refreshAccessToken(token: String) : RefreshTokenResponse {
        val param = JsonObject().apply {
            addProperty("refreshToken", token)
        }

        return apiService.refreshToken(param)
    }

    suspend fun getSaldo(token: String) : InfoSaldoResponse {
        val accessToken = "Bearer $token"
        return apiService.getSaldo(accessToken)
    }

    suspend fun getMutasi(token: String, startDate: String?, endDate: String?) : MutasiResponse {
        val accessToken = "Bearer $token"
        return apiService.getMutasi(accessToken, startDate, endDate)
    }

}