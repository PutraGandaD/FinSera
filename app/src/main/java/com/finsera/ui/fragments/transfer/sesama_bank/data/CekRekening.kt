package com.finsera.ui.fragments.transfer.sesama_bank.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CekRekening(
    val noRekening: String,
    val nominal: Double,
    val catatan: String? = "-"
) : Parcelable
