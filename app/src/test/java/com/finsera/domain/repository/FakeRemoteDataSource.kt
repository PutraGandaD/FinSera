package com.finsera.domain.repository

import com.finsera.common.utils.Resource
import com.finsera.data.source.remote.ApiService
import com.finsera.data.source.remote.response.info_saldo.InfoSaldoResponse
import com.finsera.data.source.remote.response.login.LoginResponse
import com.finsera.data.source.remote.response.mutasi.MutasiResponse
import com.finsera.data.source.remote.response.notifikasi.NotificationResponse
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeRemoteDataSource(private val apiService: ApiService) {
    suspend fun authLoginUser(username: String, password: String): LoginResponse {
        val param = JsonObject().apply {
            addProperty("username", username)
            addProperty("password", password)
        }

        return apiService.loginUser(param)
    }

    suspend fun getNotifikasi(token: String): Flow<Resource<NotificationResponse>>{
        return flow {
            emit(Resource.Loading())
            try{
                val accessToken = "Bearer $token"
                val response =apiService.getNotif(accessToken)
                if(response.data!=null){
                    emit(Resource.Success(response))
                }
            }catch (e:Exception){
                emit(Resource.Error(e.message.toString()))
            }
        }
    }

    suspend fun getMutasi(token: String, startDate: String?, endDate: String?, page: Int): MutasiResponse {
        val accessToken = "Bearer $token"
        return apiService.getMutasi(accessToken, startDate, endDate, page)
    }

    suspend fun getSaldo(token: String): InfoSaldoResponse {
        val accessToken = "Bearer $token"
        return apiService.getSaldo(accessToken)
    }
}