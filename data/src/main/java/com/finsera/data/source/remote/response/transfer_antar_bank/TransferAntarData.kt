package com.finsera.data.source.remote.response.transfer_antar_bank


import com.google.gson.annotations.SerializedName

data class TransferAntarData(
    @SerializedName("accountnum_recipient")
    val accountnumRecipient: String,
    @SerializedName("accountnum_sender")
    val accountnumSender: String,
    @SerializedName("admin_fee")
    val adminFee: String,
    @SerializedName("bank_name")
    val bankName: String,
    @SerializedName("name_recipient")
    val nameRecipient: String,
    @SerializedName("name_sender")
    val nameSender: String,
    @SerializedName("nominal")
    val nominal: String,
    @SerializedName("note")
    val note: String,
    @SerializedName("transaction_date")
    val transactionDate: String,
    @SerializedName("transaction_num")
    val transactionNum: String
)