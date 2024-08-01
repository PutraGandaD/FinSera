package com.finsera.domain.model

import com.google.gson.annotations.SerializedName

data class Mutasi(
    val code: Int,
    val message: String,
    val accountNumber: String,
    val amount: Double,
    val currency: String,
    val noTransaction: String,
    val transactionDate: String,
    val transactionId: Int,
    val transactionInformation: String
)
