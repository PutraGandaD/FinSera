package com.finsera.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TransferAntar(
    val transactionDate: String?,
    val note: String?,
    val nominal: String?,
    val adminFee: String?,
    val nameRecipient: String?,
    val transactionNum: String?,
    val bankName: String?,
    val accountnumRecipient: String?,
    val message: String
) : Parcelable