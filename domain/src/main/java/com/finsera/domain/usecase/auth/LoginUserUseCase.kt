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
            response.token?.let {
                repository.setUserToken(it)
            }
            emit(Resource.Success(response.message))
        } catch (t: Throwable) {
            when (t) {
                is HttpException -> {
                    val errorMessage =
                        t.response()?.errorBody()?.source()?.buffer?.snapshot()?.utf8()
                    errorMessage?.let {
                        if (errorMessage != null) {
                            val jsonObject = JSONObject(it)
                            val error = jsonObject.getString("message")

                            if (error == "Username or Password is invalid") {
                                emit(Resource.Error("Username atau Password Anda Salah"))
                            } else if (error == "Bad credentials") {
                                emit(Resource.Error("Username atau Password Anda Salah"))
                            } else {
                                emit(Resource.Error("Kesalahan pada server. Silahkan coba beberapa saat lagi."))
                            }
                        } else {
                            emit(Resource.Error("Kesalahan pada server. Silahkan coba beberapa saat lagi."))
                        }
                    }
                }

                is IOException -> {
                    emit(Resource.Error("Ada masalah pada koneksi internet anda. Silahkan coba lagi."))
                }

                is JSONException -> {
                    emit(Resource.Error("Kesalahan pada server. Silahkan coba beberapa saat lagi."))
                }
            }
        }
    }
}