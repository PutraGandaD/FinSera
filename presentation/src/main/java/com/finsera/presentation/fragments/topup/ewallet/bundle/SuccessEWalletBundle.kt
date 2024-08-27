package com.finsera.presentation.fragments.topup.ewallet.bundle

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SuccessEWalletBundle(
    val transactionNum: String,
    val transactionDate: String,
    val nameSender: String,
    val accountSender: String,
    val ewalletName: String,
    val ewalletAccountName: String,
    val ewalletAccount: String,
    val nominal: String,
    val feeAdmin: String,
    val note: String
):Parcelable
