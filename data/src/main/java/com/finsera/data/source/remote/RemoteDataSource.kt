package com.finsera.data.source.remote

import com.finsera.data.source.remote.response.cek_rekening_sesama_bank.CekRekeningResponse
import com.finsera.data.source.remote.response.info_saldo.InfoSaldoResponse
import com.finsera.data.source.remote.response.list_bank.ListBankResponse
import com.finsera.data.source.remote.response.login.LoginResponse
import com.finsera.data.source.remote.response.mutasi.MutasiResponse
import com.finsera.data.source.remote.response.refresh_token.RefreshTokenResponse
import com.finsera.data.source.remote.response.relogin.ReloginResponse
import com.finsera.data.source.remote.response.transfer_sesama_bank.TransferSesamaResponse
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.io.IOException

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

    suspend fun cekRekeningSesamaBank(token: String, norek: String) : CekRekeningResponse {
        val param = JsonObject().apply {
            addProperty("accountnum_recipient", norek)
        }

        val accessToken = "Bearer $token"

        return apiService.cekRekeningSesamaBank(accessToken, param)
    }

    suspend fun transferSesamaBank(token: String, noRekTujuan: String, nominal: Double, notes: String, mpin: String) : TransferSesamaResponse {
        val param = JsonObject().apply {
            addProperty("accountnum_recipient", noRekTujuan)
            addProperty("nominal", nominal)
            addProperty("note", notes)
            addProperty("pin", mpin)
        }

        val accessToken = "Bearer $token"

        return apiService.transferSesamaBank(accessToken, param)
    }

    suspend fun downloadMutasi(token: String, startDate: String, endDate: String): ResponseBody {
        val accessToken = "Bearer $token"
        return try {
            apiService.getDownloadMutasi(accessToken, startDate, endDate)
        } catch (e: HttpException) {
            throw IOException("Server error")
        } catch (e: IOException) {
            throw IOException("Network error")
        }
    }

    suspend fun getListBank(token: String) : ListBankResponse {
        val accessToken = "Bearer $token"
        return apiService.getListBank(accessToken)
    }

}