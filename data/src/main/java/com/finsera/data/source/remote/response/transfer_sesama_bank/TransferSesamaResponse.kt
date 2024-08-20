package com.finsera.data.source.remote.response.transfer_sesama_bank


import com.google.gson.annotations.SerializedName

data class TransferSesamaResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val data: TransferSesamaData?,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
)