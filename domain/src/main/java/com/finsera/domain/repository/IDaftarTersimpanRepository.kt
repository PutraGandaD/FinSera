package com.finsera.domain.repository

import com.finsera.domain.model.DaftarTersimpanAntar
import com.finsera.domain.model.DaftarTersimpanSesama

interface IDaftarTersimpanRepository {
    suspend fun getDaftarTersimpanSesama() : List<DaftarTersimpanSesama>
    suspend fun getDaftarTersimpanAntar() : List<DaftarTersimpanAntar>

    suspend fun searchDaftarTersimpanSesama(keyword: String) : List<DaftarTersimpanSesama>
    suspend fun insertDaftarTersimpanSesama(daftarTersimpan: DaftarTersimpanSesama)
    suspend fun updateDaftarTersimpanSesama(daftarTersimpan: DaftarTersimpanSesama)
    suspend fun deleteDaftarTersimpanSesama(daftarTersimpan: DaftarTersimpanSesama)

    suspend fun searchDaftarTersimpanAntar(keyword: String) : List<DaftarTersimpanAntar>
    suspend fun insertDaftarTersimpanAntar(daftarTersimpan: DaftarTersimpanAntar)
    suspend fun updateDaftarTersimpanAntar(daftarTersimpan: DaftarTersimpanAntar)
    suspend fun deleteDaftarTersimpanAntar(daftarTersimpan: DaftarTersimpanAntar)
}