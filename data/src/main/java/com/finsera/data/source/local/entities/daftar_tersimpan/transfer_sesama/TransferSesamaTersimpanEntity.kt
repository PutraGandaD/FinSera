package com.finsera.data.source.local.entities.daftar_tersimpan.transfer_sesama

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "transfer_sesama_bank")
data class TransferSesamaTersimpanEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "nama_pemilik_rekening") val namaPemilikRekening: String,
    @ColumnInfo(name = "nomor_rekening") val nomorRekening: String,
) : Parcelable