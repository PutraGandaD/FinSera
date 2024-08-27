package com.finsera.presentation.fragments.topup.ewallet.bundle

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChooseEWalletBundle (
    val ewalletId: Int,
    val ewalletName: String,
):Parcelable