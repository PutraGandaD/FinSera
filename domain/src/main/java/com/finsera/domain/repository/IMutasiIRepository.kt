package com.finsera.domain.repository

interface IMutasiIRepository {
    suspend fun getMutasi(startDate: String ?, endDate: String?,page: Int,size: Int?)
}