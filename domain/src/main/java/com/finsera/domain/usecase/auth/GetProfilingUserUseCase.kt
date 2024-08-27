package com.finsera.domain.usecase.auth

import com.finsera.common.utils.Resource
import com.finsera.domain.model.Profiling
import com.finsera.domain.repository.IAuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException

class GetProfilingUserUseCase(
    private val authRepository: IAuthRepository,
) {
    suspend operator fun invoke() : Flow<Resource<Profiling>> = flow {
        emit(Resource.Loading())
        try {
            val accessToken = authRepository.getAccessToken()
            val request = authRepository.getUserProfiling(accessToken)
            emit(Resource.Success(request))
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
                                    val accessToken = authRepository.getAccessToken()
                                    val request = authRepository.getUserProfiling(accessToken)
                                    emit(Resource.Success(request))
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