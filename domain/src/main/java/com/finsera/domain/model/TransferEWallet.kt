package com.finsera.domain.model

data class TransferEWallet(
    val message: String,
    val feeAdmin : String?,
    val note : String?,
    val accountSender : String?,
    val transactionNum : String?,
    val ewalletName : String?,
    val nominal : String?,
    val nameSender : String?,
    val ewalletAccountName : String?,
    val transactionDate : String?,
    val ewalletAccount : String?
)
