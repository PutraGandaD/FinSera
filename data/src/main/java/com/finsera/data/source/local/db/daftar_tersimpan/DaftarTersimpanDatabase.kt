package com.finsera.data.source.local.db.daftar_tersimpan

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.finsera.data.source.local.dao.daftar_tersimpan.transfer_antar.TransferAntarTersimpanDao
import com.finsera.data.source.local.dao.daftar_tersimpan.transfer_sesama.TransferSesamaTersimpanDao
import com.finsera.data.source.local.entities.daftar_tersimpan.transfer_antar.TransferAntarTersimpanEntity
import com.finsera.data.source.local.entities.daftar_tersimpan.transfer_sesama.TransferSesamaTersimpanEntity
import kotlinx.coroutines.CoroutineScope

@Database(
    entities = arrayOf(
        TransferSesamaTersimpanEntity::class,
        TransferAntarTersimpanEntity::class),
    version = 1,
    exportSchema = false)
public abstract class DaftarTersimpanDatabase : RoomDatabase() {
    abstract fun transferSesamaTersimpanDao() : TransferSesamaTersimpanDao
    abstract fun transferAntarTersimpanDao() : TransferAntarTersimpanDao
}