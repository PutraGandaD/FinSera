package com.finsera.domain.model

data class TransferVa(
    val message : String,
    val transactionNum: String?,
    val nominal: String?,
    val recipientName: String?,
    val adminFee: String?,
    val transactionDate: String?,
    val type: String?,
    val recipientVirtualAccountNum: String?
)
