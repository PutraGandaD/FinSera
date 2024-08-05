package com.finsera.domain.usecase.mutasi

import com.finsera.common.utils.Resource
import com.finsera.domain.model.Mutasi
import com.finsera.domain.repository.IMutasiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

class MutasiUseCase (private val mutasiRepository: IMutasiRepository) {
    suspend operator fun invoke(startDate: String?, endDate: String?) : Flow<Resource<List<Mutasi>?>> = flow {
        emit(Resource.Loading())
        try {
            val response = mutasiRepository.getMutasi(startDate, endDate)
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
                                "Transaction not found" -> emit(Resource.Error("Riwayat transaksi tidak ditemukan."))
                                "JWT Token has expired" -> emit(Resource.Error("Sesi Anda Habis"))
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