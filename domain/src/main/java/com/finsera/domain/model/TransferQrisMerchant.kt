package com.finsera.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TransferQrisMerchant(
    val transactionDate: String?,
    val nominal: String?,
    val nameRecipient: String?,
    val transactionNum: String?,
    val accountnumRecipient: String?,
    val message: String
) : Parcelable
