package com.finsera.domain.usecase.qris

import com.finsera.common.utils.Resource
import com.finsera.domain.model.TransferQrisMerchant
import com.finsera.domain.repository.IAuthRepository
import com.finsera.domain.repository.ITransferRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

class TransferQrisMerchantUseCase(
    private val authRepository: IAuthRepository,
    private val transferRepository: ITransferRepository
) {
    suspend operator fun invoke(
        merchantNo: String,
        merchantName: String,
        nominal: Double,
        pin: String
    ) : Flow<Resource<TransferQrisMerchant>> = flow {
        emit(Resource.Loading())
        try {
            val transfer = transferRepository.transferQrisMerchant(
                merchantNo, merchantName, nominal, pin
            )
            emit(Resource.Success(transfer))
        } catch (t: Throwable) {
            when(t) {
                is HttpException -> {
                    val response =
                        t.response()?.errorBody()?.source()?.buffer?.snapshot()?.utf8()
                    if (response != null) {
                        try {
                            val jsonObject = JSONObject(response)
                            val error = jsonObject.getInt("code")
                            val message = jsonObject.getString("message")

                            when (error) {
                                400 -> emit(Resource.Error(message))
                                401 -> {
                                    if(message == "JWT Token sudah kedaluwarsa") {
                                        val getRefreshToken = authRepository.getRefreshToken()
                                        authRepository.refreshAccessToken(getRefreshToken)
                                        val transfer = transferRepository.transferQrisMerchant(
                                            merchantNo, merchantName, nominal, pin
                                        )
                                        emit(Resource.Success(transfer))
                                    } else {
                                        emit(Resource.Error(message))
                                    }
                                }
                                402 -> emit(Resource.Error(message))
                                404 -> emit(Resource.Error(message))
                                else -> emit(Resource.Error("Kesalahan pada server. Silahkan coba beberapa saat lagi."))
                            }
                        } catch (e: Exception) {
                            when (e) {
                                is JSONException -> {
                                    emit(Resource.Error("Kesalahan pada server. Silahkan coba beberapa saat lagi."))
                                }
                            }
                        }
                    } else {
                        emit(Resource.Error("Kesalahan pada server. Silahkan coba beberapa saat lagi."))
                    }
                }

                is IOException -> {
                    emit(Resource.Error("Ada masalah pada koneksi internet anda. Silahkan coba lagi."))
                }
            }
        }
    }
}