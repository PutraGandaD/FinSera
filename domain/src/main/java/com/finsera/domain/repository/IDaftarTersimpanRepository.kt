package com.finsera.domain.repository

import com.finsera.domain.model.DaftarTersimpanAntar
import com.finsera.domain.model.DaftarTersimpanEWallet
import com.finsera.domain.model.DaftarTersimpanSesama
import com.finsera.domain.model.DaftarTersimpanVa

interface IDaftarTersimpanRepository {
    suspend fun getDaftarTersimpanSesama() : List<DaftarTersimpanSesama>
    suspend fun getDaftarTersimpanAntar() : List<DaftarTersimpanAntar>
    suspend fun getDaftarTersimpanVa(): List<DaftarTersimpanVa>
    suspend fun getDaftarTersimpanEWallet(): List<DaftarTersimpanEWallet>

    suspend fun searchDaftarTersimpanSesama(keyword: String) : List<DaftarTersimpanSesama>
    suspend fun insertDaftarTersimpanSesama(daftarTersimpan: DaftarTersimpanSesama)
    suspend fun updateDaftarTersimpanSesama(daftarTersimpan: DaftarTersimpanSesama)
    suspend fun deleteDaftarTersimpanSesama(daftarTersimpan: DaftarTersimpanSesama)

    suspend fun searchDaftarTersimpanAntar(keyword: String) : List<DaftarTersimpanAntar>
    suspend fun insertDaftarTersimpanAntar(daftarTersimpan: DaftarTersimpanAntar)
    suspend fun updateDaftarTersimpanAntar(daftarTersimpan: DaftarTersimpanAntar)
    suspend fun deleteDaftarTersimpanAntar(daftarTersimpan: DaftarTersimpanAntar)

    suspend fun searchDaftarTersimpanVa(keyword: String) : List<DaftarTersimpanVa>
    suspend fun insertDaftarTersimpanVa(daftarTersimpan: DaftarTersimpanVa)
    suspend fun updateDaftarTersimpanVa(daftarTersimpan: DaftarTersimpanVa)
    suspend fun deleteDaftarTersimpanVa(daftarTersimpan: DaftarTersimpanVa)

    suspend fun searchDaftarTersimpanEWallet(keyword: String) : List<DaftarTersimpanEWallet>
    suspend fun insertDaftarTersimpanEWallet(daftarTersimpan: DaftarTersimpanEWallet)
    suspend fun updateDaftarTersimpanEWallet(daftarTersimpan: DaftarTersimpanEWallet)
    suspend fun deleteDaftarTersimpanEWallet(daftarTersimpan: DaftarTersimpanEWallet)
}