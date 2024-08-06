package com.finsera.domain.usecase.transfer_sesama

import com.finsera.common.utils.Resource
import com.finsera.domain.model.TransferSesama
import com.finsera.domain.repository.IAuthRepository
import com.finsera.domain.repository.ITransferRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

class TransferSesamaBankUseCase(
    private val authRepository: IAuthRepository,
    private val transferRepository: ITransferRepository
) {
    suspend operator fun invoke(noRekTujuan: String, nominal: Double, note: String, mpin: String) : Flow<Resource<TransferSesama>> = flow {
        emit(Resource.Loading())
        try {
            val response = transferRepository.transferSesamaBank(
                noRekTujuan, nominal, note, mpin
            )
            emit(Resource.Success(response))
        } catch (t: Throwable) {
            when (t) {
                is HttpException -> {
                    val response =
                        t.response()?.errorBody()?.source()?.buffer?.snapshot()?.utf8()
                    if (response != null) {
                        try {
                            val jsonObject = JSONObject(response)
                            val error = jsonObject.getString("message")

                            when (error) {
                                "Nomor Rekening Tidak Ditemukan" -> emit(Resource.Error("Nomor Rekening Tidak Ditemukan"))
                                "JWT Token has expired" -> {
                                    val getRefreshToken = authRepository.getRefreshToken()
                                    authRepository.refreshAccessToken(getRefreshToken)
                                    val response = transferRepository.transferSesamaBank(
                                        noRekTujuan, nominal, note, mpin
                                    )
                                    emit(Resource.Success(response))
                                }
                                "Pin Anda Salah" -> emit(Resource.Error("PIN Anda Salah. Silahkan input ulang PIN anda."))
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