package com.finsera.data.source.local.dao.daftar_tersimpan.ewallet

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.finsera.data.source.local.entities.daftar_tersimpan.ewallet.TransferEWalletTersimpanEntity

@Dao
interface TransferEWalletTersimpanDao {
    @Query("SELECT * FROM transfer_ewallet")
    suspend fun getDaftarEWallet() : List<TransferEWalletTersimpanEntity>

    @Query("SELECT * FROM transfer_ewallet WHERE nama_akun_ewallet LIKE '%' || :keyword || '%' OR nomor_ewallet LIKE '%' || :keyword || '%'")
    suspend fun searchDaftarEWallet(keyword: String) : List<TransferEWalletTersimpanEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDaftarEWallet(eWalletTersimpanEntity: TransferEWalletTersimpanEntity)

    @Update
    suspend fun updateDaftarEWallet(eWalletTersimpanEntity: TransferEWalletTersimpanEntity)

    @Delete
    suspend fun deleteDaftarEWallet(eWalletTersimpanEntity: TransferEWalletTersimpanEntity)
    
}