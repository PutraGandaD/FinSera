package com.finsera.data.source.local.entities.daftar_tersimpan.ewallet

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "transfer_ewallet")
data class TransferEWalletTersimpanEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "nama_ewallet")
    val namaEWallet: String,

    @ColumnInfo(name = "id_akun_ewallet")
    val idAkunEWallet: Int,

    @ColumnInfo(name = "nomor_ewallet")
    val nomorEWallet: String,

    @ColumnInfo(name = "nama_akun_ewallet")
    val namaAkunEWallet: String,

    ) : Parcelable
