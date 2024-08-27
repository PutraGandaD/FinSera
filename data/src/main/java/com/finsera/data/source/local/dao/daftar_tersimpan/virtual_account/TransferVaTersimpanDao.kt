package com.finsera.data.source.local.dao.daftar_tersimpan.virtual_account

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.finsera.data.source.local.entities.daftar_tersimpan.virtual_account.TransferVaTersimpanEntity

@Dao
interface TransferVaTersimpanDao {
    @Query("SELECT * FROM virtual_account")
    suspend fun getDaftarVirtualAccount() : List<TransferVaTersimpanEntity>

    @Query("SELECT * FROM virtual_account WHERE nama_virtual_account LIKE '%' || :keyword || '%' OR nomor_virtual_account LIKE '%' || :keyword || '%'")
    suspend fun searchDaftarVirtualAccount(keyword: String) : List<TransferVaTersimpanEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDaftarVirtualAccount(virtualAccountTersimpanEntity: TransferVaTersimpanEntity)

    @Update
    suspend fun updateDaftarVirtualAccount(virtualAccountTersimpanEntity: TransferVaTersimpanEntity)

    @Delete
    suspend fun deleteDaftarVirtualAccount(virtualAccountTersimpanEntity: TransferVaTersimpanEntity)
}

