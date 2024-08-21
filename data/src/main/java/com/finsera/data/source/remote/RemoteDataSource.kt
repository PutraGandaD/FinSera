package com.finsera.data.source.remote

import com.finsera.data.source.remote.response.cek_rekening_antar_bank.CekRekeningAntarResponse
import com.finsera.data.source.remote.response.cek_rekening_sesama_bank.CekRekeningSesamaResponse
import com.finsera.common.utils.Resource
import com.finsera.data.source.remote.response.info_saldo.InfoSaldoResponse
import com.finsera.data.source.remote.response.list_bank.ListBankResponse
import com.finsera.data.source.remote.response.login.LoginResponse
import com.finsera.data.source.remote.response.mutasi.MutasiResponse
import com.finsera.data.source.remote.response.qris.ScanQrisResponse
import com.finsera.data.source.remote.response.refresh_token.RefreshTokenResponse
import com.finsera.data.source.remote.response.relogin.ReloginResponse
import com.finsera.data.source.remote.response.transfer_antar_bank.TransferAntarResponse
import com.finsera.data.source.remote.response.transfer_sesama_bank.TransferSesamaResponse
import com.finsera.data.source.remote.response.virtual_account.CheckVaResponse
import com.finsera.data.source.remote.response.virtual_account.TransferVaResponse
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

    suspend fun cekRekeningSesamaBank(token: String, norek: String) : CekRekeningSesamaResponse {
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

                            403 -> {
                                emit(Resource.Error("Kesalahan pada server"))
                            }

                            500 -> {
                                emit(Resource.Error("Terjadi Kesalahan pada server"))
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

    suspend fun transferVirtualAccount(
        token: String,
        vaAccountNum: String,
        pin: String
    ): Flow<Resource<TransferVaResponse>> {
        return flow<Resource<TransferVaResponse>> {
            emit(Resource.Loading())
            try {
                val param = JsonObject().apply {
                    addProperty("virtualAccountNumber", vaAccountNum)
                    addProperty("mpinAccount", pin)
                }

                val accessToken = "Bearer $token"
                val response = apiService.transferVirtualAccount(accessToken, param)
                val message = response.message

                if (response.data != null) {
                    emit(Resource.Success(response))
                } else {
                    when (message) {
                        "Pin is Invalid" -> emit(Resource.Error("Pin yang dimasukkan salah"))
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
                                emit(Resource.Error("Pin yang dimasukkan salah"))
                            }

                            401 -> {
                                emit(Resource.Error("Sesi telah habis"))
                            }

                            403 -> {
                                emit(Resource.Error("Virtual Account tidak ditemukan"))
                            }

                            500 -> {
                                emit(Resource.Error("Terjadi Kesalahan pada server"))
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

    suspend fun getListBank(token: String) : ListBankResponse {
        val accessToken = "Bearer $token"
        return apiService.getListBank(accessToken)
    }

    suspend fun cekRekeningAntarBank(token: String, idBank: Int, norek: String) : CekRekeningAntarResponse {
        val param = JsonObject().apply {
            addProperty("bank_id", idBank)
            addProperty("accountnum_recipient", norek)
        }

        val accessToken = "Bearer $token"

        return apiService.cekRekeningAntarBank(accessToken, param)
    }

    suspend fun transferAntarBank(token: String, idBank: Int, noRek: String, nominal: Double, note: String, pin: String) : TransferAntarResponse {
        val param = JsonObject().apply {
            addProperty("bank_id", idBank)
            addProperty("accountnum_recipient", noRek)
            addProperty("nominal", nominal)
            addProperty("note", note)
            addProperty("pin", pin)
        }

        val accessToken = "Bearer $token"

        return apiService.transferAntarBank(accessToken, param)
    }

    suspend fun transferQrisMerchant(token: String, merchantNo: String, merchantName: String, nominal: Double, pin: String) : ScanQrisResponse {
        val param = JsonObject().apply {
            addProperty("merchantNo", merchantNo)
            addProperty("merchantName", merchantName)
            addProperty("nominal", nominal)
            addProperty("pin", pin)
        }

        val accessToken = "Bearer $token"

        return apiService.transferQrisMerchant(accessToken, param)
    }
}