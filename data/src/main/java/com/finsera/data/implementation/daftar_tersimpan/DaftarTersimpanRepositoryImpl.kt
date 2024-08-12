package com.finsera.data.implementation.daftar_tersimpan

import com.finsera.data.source.local.dao.daftar_tersimpan.transfer_antar.TransferAntarTersimpanDao
import com.finsera.data.source.local.dao.daftar_tersimpan.transfer_sesama.TransferSesamaTersimpanDao
import com.finsera.data.source.local.entities.daftar_tersimpan.transfer_sesama.TransferSesamaTersimpanEntity
import com.finsera.data.utils.DataMapper
import com.finsera.domain.model.DaftarTersimpan
import com.finsera.domain.repository.IDaftarTersimpanRepository

class DaftarTersimpanRepositoryImpl(
    private val daftarTersimpanSesamaDao: TransferSesamaTersimpanDao,
    private val daftarTersimpanAntarDao: TransferAntarTersimpanDao
) : IDaftarTersimpanRepository{
    override suspend fun getDaftarTersimpanSesama(): List<DaftarTersimpan> {
        val data = daftarTersimpanSesamaDao.getDaftarTersimpanSesama()

        return DataMapper.daftarTersimpanSesamaToDomain(data)
    }

    override suspend fun getDaftarTersimpanAntar(): List<DaftarTersimpan> {
        TODO("Not yet implemented")
    }

    override suspend fun searchDaftarTersimpanSesama(keyword: String): List<DaftarTersimpan> {
        val data = daftarTersimpanSesamaDao.searchDaftarTersimpanSesama(keyword)

        return DataMapper.daftarTersimpanSesamaToDomain(data)
    }

    override suspend fun insertDaftarTersimpanSesama(daftarTersimpan: DaftarTersimpan) {
        daftarTersimpanSesamaDao.insertDaftarTersimpanSesama(
            TransferSesamaTersimpanEntity(
                id = 0,
                namaPemilikRekening = daftarTersimpan.namaPemilikRekening,
                nomorRekening = daftarTersimpan.noRekening
            )
        )
    }

    override suspend fun updateDaftarTersimpanSesama(daftarTersimpan: DaftarTersimpan) {
        daftarTersimpanSesamaDao.updateDaftarTersimpanSesama(
            TransferSesamaTersimpanEntity(
                id = daftarTersimpan.id,
                namaPemilikRekening = daftarTersimpan.namaPemilikRekening,
                nomorRekening = daftarTersimpan.noRekening
            )
        )
    }

    override suspend fun deleteDaftarTersimpanSesama(daftarTersimpan: DaftarTersimpan) {
        daftarTersimpanSesamaDao.deleteDaftarTersimpanSesama(
            TransferSesamaTersimpanEntity(
                id = daftarTersimpan.id,
                namaPemilikRekening = daftarTersimpan.namaPemilikRekening,
                nomorRekening = daftarTersimpan.noRekening
            )
        )
    }
}