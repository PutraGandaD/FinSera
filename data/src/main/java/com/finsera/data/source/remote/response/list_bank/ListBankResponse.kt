package com.finsera.data.source.remote.response.list_bank


import com.google.gson.annotations.SerializedName

data class ListBankResponse(
    @SerializedName("data")
    val data: List<ListBankData>,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Int
)