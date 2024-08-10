package com.finsera.data.source.local.entities.daftar_tersimpan.transfer_antar

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "transfer_antar_bank")
data class TransferAntarTersimpanEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "nama_pemilik_rekening") val namaPemilikRekening: String,
    @ColumnInfo(name = "nomor_rekening") val nomorRekening: String,
) : Parcelable