package com.finsera.data.source.local.dao.daftar_tersimpan.transfer_sesama

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.finsera.data.source.local.entities.daftar_tersimpan.transfer_sesama.TransferSesamaTersimpanEntity

@Dao
interface TransferSesamaTersimpanDao {
    @Query("SELECT * FROM transfer_sesama_bank")
    suspend fun getDaftarTersimpanSesama() : List<TransferSesamaTersimpanEntity>

    @Query("SELECT * FROM transfer_sesama_bank WHERE nama_pemilik_rekening LIKE '%' || :keyword || '%' OR nomor_rekening LIKE '%' || :keyword || '%'")
    suspend fun searchDaftarTersimpanSesama(keyword: String) : List<TransferSesamaTersimpanEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDaftarTersimpanSesama(transferSesamaTersimpanEntity: TransferSesamaTersimpanEntity)

    @Update
    suspend fun updateDaftarTersimpanSesama(transferSesamaTersimpanEntity: TransferSesamaTersimpanEntity)

    @Delete
    suspend fun deleteDaftarTersimpanSesama(transferSesamaTersimpanEntity: TransferSesamaTersimpanEntity)
}