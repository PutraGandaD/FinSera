package com.finsera.data.implementation.daftar_tersimpan

import com.finsera.data.source.local.dao.daftar_tersimpan.transfer_antar.TransferAntarTersimpanDao
import com.finsera.data.source.local.dao.daftar_tersimpan.transfer_sesama.TransferSesamaTersimpanDao
import com.finsera.data.source.local.dao.daftar_tersimpan.virutal_account.TransferVaTersimpanDao
import com.finsera.data.source.local.entities.daftar_tersimpan.transfer_sesama.TransferSesamaTersimpanEntity
import com.finsera.data.source.local.entities.daftar_tersimpan.virtual_account.TransferVaTersimpanEntity
import com.finsera.data.utils.DataMapper
import com.finsera.domain.model.DaftarTersimpan
import com.finsera.domain.repository.IDaftarTersimpanRepository

class DaftarTersimpanRepositoryImpl(
    private val daftarTersimpanSesamaDao: TransferSesamaTersimpanDao,
    private val daftarTersimpanAntarDao: TransferAntarTersimpanDao,
    private val daftarTersimpanVaDao: TransferVaTersimpanDao
) : IDaftarTersimpanRepository{
    override suspend fun getDaftarTersimpanSesama(): List<DaftarTersimpan> {
        val data = daftarTersimpanSesamaDao.getDaftarTersimpanSesama()

        return DataMapper.daftarTersimpanSesamaToDomain(data)
    }

    override suspend fun getDaftarTersimpanAntar(): List<DaftarTersimpan> {
        TODO("Not yet implemented")
    }

    override suspend fun getDaftarTersimpanVa(): List<DaftarTersimpan> {
        val data = daftarTersimpanVaDao.getDaftarVirtualAccount()

        return DataMapper.daftarTersimpanVaToDomain(data)
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

    override suspend fun searchDaftarTersimpanVa(keyword: String): List<DaftarTersimpan> {
        val data = daftarTersimpanVaDao.searchDaftarVirtualAccount(keyword)

        return DataMapper.daftarTersimpanVaToDomain(data)
    }

    override suspend fun insertDaftarTersimpanVa(daftarTersimpan: DaftarTersimpan) {
        daftarTersimpanVaDao.insertDaftarVirtualAccount(
            TransferVaTersimpanEntity(
                id = daftarTersimpan.id,
                namaVa = daftarTersimpan.namaPemilikRekening,
                nomorVa = daftarTersimpan.noRekening,
                nominalVa = daftarTersimpan.nominal!!
            )
        )
    }

    override suspend fun updateDaftarTersimpanVa(daftarTersimpan: DaftarTersimpan) {
        daftarTersimpanVaDao.updateDaftarVirtualAccount(
            TransferVaTersimpanEntity(
                id = daftarTersimpan.id,
                namaVa = daftarTersimpan.namaPemilikRekening,
                nomorVa = daftarTersimpan.noRekening,
                nominalVa = daftarTersimpan.nominal!!
            )
        )
    }

    override suspend fun deleteDaftarTersimpanVa(daftarTersimpan: DaftarTersimpan) {
        daftarTersimpanVaDao.deleteDaftarVirtualAccount(
            TransferVaTersimpanEntity(
                id = daftarTersimpan.id,
                namaVa = daftarTersimpan.namaPemilikRekening,
                nomorVa = daftarTersimpan.noRekening,
                nominalVa = daftarTersimpan.nominal!!
            )
        )
    }
}