package com.finsera.data.source.remote.response.list_bank


import com.google.gson.annotations.SerializedName

data class ListBankData(
    @SerializedName("bank_code")
    val bankCode: String,
    @SerializedName("bank_id")
    val bankId: Int,
    @SerializedName("bank_image")
    val bankImage: Any,
    @SerializedName("bank_name")
    val bankName: String
)