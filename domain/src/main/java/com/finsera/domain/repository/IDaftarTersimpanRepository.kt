package com.finsera.domain.repository

import com.finsera.domain.model.DaftarTersimpan
import kotlinx.coroutines.flow.Flow

interface IDaftarTersimpanRepository {
    suspend fun getDaftarTersimpanSesama() : List<DaftarTersimpan>
    suspend fun getDaftarTersimpanAntar() : List<DaftarTersimpan>
    suspend fun getDaftarTersimpanVa(): List<DaftarTersimpan>


    suspend fun searchDaftarTersimpanSesama(keyword: String) : List<DaftarTersimpan>
    suspend fun insertDaftarTersimpanSesama(daftarTersimpan: DaftarTersimpan)
    suspend fun updateDaftarTersimpanSesama(daftarTersimpan: DaftarTersimpan)
    suspend fun deleteDaftarTersimpanSesama(daftarTersimpan: DaftarTersimpan)


    suspend fun searchDaftarTersimpanVa(keyword: String) : List<DaftarTersimpan>
    suspend fun insertDaftarTersimpanVa(daftarTersimpan: DaftarTersimpan)
    suspend fun updateDaftarTersimpanVa(daftarTersimpan: DaftarTersimpan)
    suspend fun deleteDaftarTersimpanVa(daftarTersimpan: DaftarTersimpan)

}