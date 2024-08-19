package com.finsera.data.implementation.daftar_tersimpan

import com.finsera.data.source.local.dao.daftar_tersimpan.transfer_antar.TransferAntarTersimpanDao
import com.finsera.data.source.local.dao.daftar_tersimpan.transfer_sesama.TransferSesamaTersimpanDao
import com.finsera.data.source.local.entities.daftar_tersimpan.transfer_antar.TransferAntarTersimpanEntity
import com.finsera.data.source.local.entities.daftar_tersimpan.transfer_sesama.TransferSesamaTersimpanEntity
import com.finsera.data.utils.DataMapper
import com.finsera.domain.model.DaftarTersimpanAntar
import com.finsera.domain.model.DaftarTersimpanSesama
import com.finsera.domain.repository.IDaftarTersimpanRepository

class DaftarTersimpanRepositoryImpl(
    private val daftarTersimpanSesamaDao: TransferSesamaTersimpanDao,
    private val daftarTersimpanAntarDao: TransferAntarTersimpanDao
) : IDaftarTersimpanRepository{
    override suspend fun getDaftarTersimpanSesama(): List<DaftarTersimpanSesama> {
        val data = daftarTersimpanSesamaDao.getDaftarTersimpanSesama()

        return DataMapper.daftarTersimpanSesamaToDomain(data)
    }

    override suspend fun getDaftarTersimpanAntar(): List<DaftarTersimpanAntar> {
        val data = daftarTersimpanAntarDao.getDaftarTersimpanAntar()

        return DataMapper.daftarTersimpanAntarToDomain(data)
    }

    override suspend fun searchDaftarTersimpanSesama(keyword: String): List<DaftarTersimpanSesama> {
        val data = daftarTersimpanSesamaDao.searchDaftarTersimpanSesama(keyword)

        return DataMapper.daftarTersimpanSesamaToDomain(data)
    }

    override suspend fun insertDaftarTersimpanSesama(daftarTersimpan: DaftarTersimpanSesama) {
        daftarTersimpanSesamaDao.insertDaftarTersimpanSesama(
            TransferSesamaTersimpanEntity(
                id = 0,
                namaPemilikRekening = daftarTersimpan.namaPemilikRekening,
                nomorRekening = daftarTersimpan.noRekening
            )
        )
    }

    override suspend fun updateDaftarTersimpanSesama(daftarTersimpan: DaftarTersimpanSesama) {
        daftarTersimpanSesamaDao.updateDaftarTersimpanSesama(
            TransferSesamaTersimpanEntity(
                id = daftarTersimpan.id,
                namaPemilikRekening = daftarTersimpan.namaPemilikRekening,
                nomorRekening = daftarTersimpan.noRekening
            )
        )
    }

    override suspend fun deleteDaftarTersimpanSesama(daftarTersimpan: DaftarTersimpanSesama) {
        daftarTersimpanSesamaDao.deleteDaftarTersimpanSesama(
            TransferSesamaTersimpanEntity(
                id = daftarTersimpan.id,
                namaPemilikRekening = daftarTersimpan.namaPemilikRekening,
                nomorRekening = daftarTersimpan.noRekening
            )
        )
    }

    override suspend fun searchDaftarTersimpanAntar(keyword: String): List<DaftarTersimpanAntar> {
        val data = daftarTersimpanAntarDao.searchDaftarTersimpanAntar(keyword)

        return DataMapper.daftarTersimpanAntarToDomain(data)
    }

    override suspend fun insertDaftarTersimpanAntar(daftarTersimpan: DaftarTersimpanAntar) {
        daftarTersimpanAntarDao.insertDaftarTersimpanAntar(
            TransferAntarTersimpanEntity(
                id = 0,
                idBank = daftarTersimpan.idBank,
                namaBank = daftarTersimpan.namaBank,
                namaPemilikRekening = daftarTersimpan.namaPemilikRekening,
                nomorRekening = daftarTersimpan.noRekening
            )
        )
    }

    override suspend fun updateDaftarTersimpanAntar(daftarTersimpan: DaftarTersimpanAntar) {
        daftarTersimpanAntarDao.updateDaftarTersimpanAntar(
            TransferAntarTersimpanEntity(
                id = daftarTersimpan.id,
                idBank = daftarTersimpan.idBank,
                namaBank = daftarTersimpan.namaBank,
                namaPemilikRekening = daftarTersimpan.namaPemilikRekening,
                nomorRekening = daftarTersimpan.noRekening
            )
        )
    }

    override suspend fun deleteDaftarTersimpanAntar(daftarTersimpan: DaftarTersimpanAntar) {
        daftarTersimpanAntarDao.deleteDaftarTersimpanAntar(
            TransferAntarTersimpanEntity(
                id = daftarTersimpan.id,
                idBank = daftarTersimpan.idBank,
                namaBank = daftarTersimpan.namaBank,
                namaPemilikRekening = daftarTersimpan.namaPemilikRekening,
                nomorRekening = daftarTersimpan.noRekening
            )
        )
    }
}