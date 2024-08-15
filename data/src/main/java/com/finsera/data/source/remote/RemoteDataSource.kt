package com.finsera.data.source.remote

import com.finsera.common.utils.Resource
import com.finsera.data.source.remote.response.cek_rekening_sesama_bank.CekRekeningResponse
import com.finsera.data.source.remote.response.info_saldo.InfoSaldoResponse
import com.finsera.data.source.remote.response.login.LoginResponse
import com.finsera.data.source.remote.response.mutasi.MutasiResponse
import com.finsera.data.source.remote.response.refresh_token.RefreshTokenResponse
import com.finsera.data.source.remote.response.relogin.ReloginResponse
import com.finsera.data.source.remote.response.transfer_sesama_bank.TransferSesamaResponse
import com.finsera.data.source.remote.response.virtual_account.CheckVaResponse
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.io.IOException

class RemoteDataSource(private val apiService: ApiService) {
    suspend fun authLoginUser(username: String, password: String): LoginResponse {
        val param = JsonObject().apply {
            addProperty("username", username)
            addProperty("password", password)
        }

        return apiService.loginUser(param)
    }

    suspend fun reloginUser(token: String, mpin: String): ReloginResponse {
        val param = JsonObject().apply {
            addProperty("mpinAuth", mpin)
        }

        val accessToken = "Bearer $token"

        return apiService.reloginUser(accessToken, param)
    }

    suspend fun refreshAccessToken(token: String): RefreshTokenResponse {
        val param = JsonObject().apply {
            addProperty("refreshToken", token)
        }

        return apiService.refreshToken(param)
    }

    suspend fun getSaldo(token: String): InfoSaldoResponse {
        val accessToken = "Bearer $token"
        return apiService.getSaldo(accessToken)
    }

    suspend fun getMutasi(token: String, startDate: String?, endDate: String?): MutasiResponse {
        val accessToken = "Bearer $token"
        return apiService.getMutasi(accessToken, startDate, endDate)
    }

    suspend fun cekRekeningSesamaBank(token: String, norek: String): CekRekeningResponse {
        val param = JsonObject().apply {
            addProperty("accountnum_recipient", norek)
        }

        val accessToken = "Bearer $token"

        return apiService.cekRekeningSesamaBank(accessToken, param)
    }

    suspend fun transferSesamaBank(
        token: String,
        noRekTujuan: String,
        nominal: Double,
        notes: String,
        mpin: String
    ): TransferSesamaResponse {
        val param = JsonObject().apply {
            addProperty("accountnum_recipient", noRekTujuan)
            addProperty("nominal", nominal)
            addProperty("note", notes)
            addProperty("pin", mpin)
        }

        val accessToken = "Bearer $token"

        return apiService.transferSesamaBank(accessToken, param)
    }


    suspend fun cekVirtualAccount(
        token: String,
        vaAccountNum: String
    ): Flow<Resource<CheckVaResponse>> {
        return flow {
            emit(Resource.Loading())
            try {
                val param = JsonObject().apply {
                    addProperty("virtualAccountNumber", vaAccountNum)
                }

                val accessToken = "Bearer $token"
                val response = apiService.cekVirtualAccount(accessToken, param)
                val message = response.message
                if (response.data != null) {
                    emit(Resource.Success(response))
                } else {
                    when (message) {
                        "Virtual Account not found" -> emit(Resource.Error("Virtual Account Tidak Ditemukan"))
                        "JWT Token has expired" -> {
                            emit(Resource.Error("JWT Token has expired"))
                        }


                        else -> emit(Resource.Error("Error"))
                    }
                }
            } catch (e: Exception) {
                when (e) {
                    is HttpException -> {
                        when (e.code()) {
                            400 -> {
                                emit(Resource.Error("Virtual Account tidak ditemukan"))
                            }
                            401 -> {
                                emit(Resource.Error("Sesi telah habis"))
                            }
                            else -> {
                                emit(Resource.Error(e.message()))
                            }
                        }
                    }

                    is IOException -> {
                        emit(Resource.Error(e.message.toString()))
                    }
                }
            }
        }.flowOn(Dispatchers.IO)
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

}