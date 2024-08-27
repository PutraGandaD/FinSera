package com.finsera.data.source.local.dao.daftar_tersimpan.transfer_antar

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.finsera.data.source.local.entities.daftar_tersimpan.transfer_antar.TransferAntarTersimpanEntity
import com.finsera.data.source.local.entities.daftar_tersimpan.transfer_sesama.TransferSesamaTersimpanEntity

@Dao
interface TransferAntarTersimpanDao {
    @Query("SELECT * FROM transfer_antar_bank")
    suspend fun getDaftarTersimpanAntar() : List<TransferAntarTersimpanEntity>

    @Query("SELECT * FROM transfer_antar_bank WHERE nama_pemilik_rekening LIKE '%' || :keyword || '%' OR nomor_rekening LIKE '%' || :keyword || '%'")
    suspend fun searchDaftarTersimpanAntar(keyword: String) : List<TransferAntarTersimpanEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDaftarTersimpanAntar(transferAntarTersimpanEntity: TransferAntarTersimpanEntity)

    @Update
    suspend fun updateDaftarTersimpanAntar(transferAntarTersimpanEntity: TransferAntarTersimpanEntity)

    @Delete
    suspend fun deleteDaftarTersimpanAntar(transferAntarTersimpanEntity: TransferAntarTersimpanEntity)
}