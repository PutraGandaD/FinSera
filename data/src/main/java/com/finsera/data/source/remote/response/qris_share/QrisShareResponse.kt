package com.finsera.data.source.remote.response.qris_share


import com.google.gson.annotations.SerializedName

data class QrisShareResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val data: QrisShareData,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
)