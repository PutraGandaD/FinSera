package com.finsera.data.source.remote.response.qris


import com.google.gson.annotations.SerializedName

data class ScanQrisData(
    @SerializedName("accountnum_recipient")
    val accountnumRecipient: String,
    @SerializedName("accountnum_sender")
    val accountnumSender: String,
    @SerializedName("name_recipient")
    val nameRecipient: String,
    @SerializedName("name_sender")
    val nameSender: String,
    @SerializedName("nominal")
    val nominal: String,
    @SerializedName("transaction_date")
    val transactionDate: String,
    @SerializedName("transaction_num")
    val transactionNum: String
)