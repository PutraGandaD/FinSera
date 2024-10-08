package com.finsera.data.source.remote.response.transfer_antar_bank


import com.google.gson.annotations.SerializedName

data class TransferAntarResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val data: TransferAntarData?,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
)