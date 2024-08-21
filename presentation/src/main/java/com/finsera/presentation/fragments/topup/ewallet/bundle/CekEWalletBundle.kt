package com.finsera.presentation.fragments.topup.ewallet.bundle

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CekEWalletBundle(
    val id: Int,
    val namaEWallet :String,
    val nomorEWallet: String,
    val namaAkunEWallet: String
):Parcelable

