package com.finsera.presentation.fragments.transfer.antar_bank.bundle

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CekRekeningAntarBundle(
    val idBank: Int?,
    val namaBank: String?,
    val namaPemilikRekening: String?,
    val noRekening: String?
) : Parcelable