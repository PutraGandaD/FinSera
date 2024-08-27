package com.finsera.domain.usecase.mutasi

import com.finsera.common.utils.Resource
import com.finsera.domain.model.Mutasi
import com.finsera.domain.repository.IAuthRepository
import com.finsera.domain.repository.IMutasiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

class MutasiUseCase (
    private val authRepository: IAuthRepository,
    private val mutasiRepository: IMutasiRepository) {
    suspend operator fun invoke(startDate: String?, endDate: String?, page: Int) : Flow<Resource<List<Mutasi>?>> = flow {
        emit(Resource.Loading())
        try {
            val response = mutasiRepository.getMutasi(startDate, endDate, page)
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

                            when(error) {
                                401 -> {
                                    val getRefreshToken = authRepository.getRefreshToken()
                                    authRepository.refreshAccessToken(getRefreshToken)
                                    val response = mutasiRepository.getMutasi(startDate, endDate, page)
                                    emit(Resource.Success(response))
                                }
                                404 -> {
                                    emit(Resource.Error(message))
                                }
                                else -> {
                                    emit(Resource.Error("Kesalahan pada server. Silahkan coba beberapa saat lagi."))
                                }
                            }
                        } catch (e: Exception) {
                            when (e) {
                                is JSONException -> {
                                    emit(Resource.Error("Kesalahan pada server. Silahkan coba beberapa saat lagi."))
                                }
                            }
                        }
                    } else {

                    }
                }

                is IOException -> {
                    emit(Resource.Error("Ada masalah pada koneksi internet anda. Silahkan coba lagi."))
                }
            }
        }
    }
}