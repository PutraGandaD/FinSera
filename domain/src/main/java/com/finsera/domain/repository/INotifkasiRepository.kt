package com.finsera.domain.repository

import com.finsera.common.utils.Resource
import com.finsera.domain.model.Notifikasi
import kotlinx.coroutines.flow.Flow

interface INotifkasiRepository {
    suspend fun getNotifikasi(): Flow<Resource<List<Notifikasi>>>
}