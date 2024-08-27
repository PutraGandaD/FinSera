package com.finsera.data.source.remote.response.qris


import com.google.gson.annotations.SerializedName

data class ScanQrisResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val data: ScanQrisData?,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
)