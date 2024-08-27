package com.finsera.domain.usecase.auth

import com.finsera.common.utils.Resource
import com.finsera.domain.repository.IAuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

class LoginUserUseCase(private val repository: IAuthRepository) {
    suspend operator fun invoke(username: String, password: String) : Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val response = repository.login(username, password)

            when(response.status) {
                "ACTIVE" -> {
                    repository.setLoginStatus(true)
                    repository.saveUsernameForRecovery(username)
                    emit(Resource.Success("Login Berhasil"))
                }
                "INACTIVE" -> {
                    repository.setLoginStatus(false)
                    emit(Resource.Error("Akun anda nonaktif / dinonaktifkan."))
                }
            }
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

                            when(error) {
                                401 -> {
                                    emit(Resource.Error(message))
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