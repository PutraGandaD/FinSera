package com.finsera.presentation.fragments.transfer.sesama_bank.bundle

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CekRekeningSesama(
    val namaPemilikRekening: String,
    val noRekening: String,
) : Parcelable
