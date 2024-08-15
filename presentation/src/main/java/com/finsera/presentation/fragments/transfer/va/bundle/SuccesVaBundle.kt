package com.finsera.presentation.fragments.transfer.va.bundle

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SuccesVaBundle(
    val transactionNum: String,
    val nominal: String,
    val recipientName: String,
    val transactionDate: String,
    val type: String,
    val recipientVirtualAccountNum: String
) : Parcelable
