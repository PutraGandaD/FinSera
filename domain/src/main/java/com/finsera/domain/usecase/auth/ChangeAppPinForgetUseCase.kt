package com.finsera.domain.usecase.auth

import com.finsera.common.utils.Resource
import com.finsera.domain.repository.IAuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

class ChangeAppPinForgetUseCase(
    private val authRepository: IAuthRepository
) {
    operator suspend fun invoke(newPin: String, confirmPin: String, accountPassword: String) : Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            if(newPin != confirmPin) {
                emit(Resource.Error("PIN baru dan Konfirmasi PIN Baru tidak sama!"))
                return@flow
            } else if(newPin.length != 6) {
                emit(Resource.Error("PIN baru harus 6 digit"))
                return@flow
            }

            val getUsernameForRecovery = authRepository.getUsernameForRecovery()
            val loginFirst = authRepository.login(getUsernameForRecovery, accountPassword)

            when(loginFirst.status) {
                "ACTIVE" -> {
                    authRepository.setAppLockPin(newPin)
                    emit(Resource.Success("Ganti PIN Aplikasi berhasil! Silahkan login dengan PIN Aplikasi anda yang baru."))
                }
                "INACTIVE" -> {
                    emit(Resource.Error("Akun anda nonaktif / dinonaktifkan."))
                }
            }
        } catch (t: Throwable) {
            when(t) {
                is HttpException -> {
                    val response =
                        t.response()?.errorBody()?.source()?.buffer?.snapshot()?.utf8()
                    if (response != null) {
                        try {
                            val jsonObject = JSONObject(response)
                            val message = jsonObject.getString("message")
                            val error = jsonObject.getInt("code")

                            when(error) {
                                401 -> {
                                    emit(Resource.Error("Password yang anda masukkan salah. Silahkan coba lagi."))
                                }
                                404 -> {
                                    emit(Resource.Error(message))
                                }
                                else -> {
                                    emit(Resource.Error("Kesalahan pada server. Silahkan coba beberapa saat lagi."))
                                }
                            }
                        } catch (e: Exception) {
                            when(e) {
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