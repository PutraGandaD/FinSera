package com.finsera.domain.usecase.transfer.sesama_bank

import com.finsera.common.utils.Resource
import com.finsera.domain.model.CekRekening
import com.finsera.domain.repository.IAuthRepository
import com.finsera.domain.repository.ITransferRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

class CekRekeningSesamaUseCase(
    private val authRepository: IAuthRepository,
    private val transferRepository: ITransferRepository
) {
    suspend operator fun invoke(noRekTujuan: String) : Flow<Resource<CekRekening>> = flow {
        emit(Resource.Loading())
        try {
            val response = transferRepository.cekDataRekeningSesama(noRekTujuan)
            emit(Resource.Success(response))
        } catch (t: Throwable) {
            when (t) {
                is HttpException -> {
                    val response =
                        t.response()?.errorBody()?.source()?.buffer?.snapshot()?.utf8()
                    if (response != null) {
                        try {
                            val jsonObject = JSONObject(response)
                            val message = jsonObject.getString("message")
                            val error = jsonObject.getInt("code")

                            when (error) {
                                404 -> emit(Resource.Error(message))
                                401 -> {
                                    val getRefreshToken = authRepository.getRefreshToken()
                                    authRepository.refreshAccessToken(getRefreshToken)
                                    val response = transferRepository.cekDataRekeningSesama(
                                        noRekTujuan
                                    )
                                    emit(Resource.Success(response))
                                }
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