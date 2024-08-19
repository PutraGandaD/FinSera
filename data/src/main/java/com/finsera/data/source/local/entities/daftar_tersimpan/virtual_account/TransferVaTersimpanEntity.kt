package com.finsera.data.source.local.entities.daftar_tersimpan.virtual_account


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "virtual_account")
data class TransferVaTersimpanEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "nama_virtual_account")
    val namaVa: String,

    @ColumnInfo(name = "nomor_virtual_account")
    val nomorVa: String,

    @ColumnInfo(name = "nominal_virtual_account")
    val nominalVa : Int
)