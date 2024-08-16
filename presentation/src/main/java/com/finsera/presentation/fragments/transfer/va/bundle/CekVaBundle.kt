package com.finsera.presentation.fragments.transfer.va.bundle

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CekVaBundle(
    val vaAccountNum: String,
    val vaAccountName: String,
    val vaTransferNominal: Int
):Parcelable
