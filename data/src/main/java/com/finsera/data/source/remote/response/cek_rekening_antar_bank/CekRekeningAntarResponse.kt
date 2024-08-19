package com.finsera.data.source.remote.response.cek_rekening_antar_bank


import com.google.gson.annotations.SerializedName

data class CekRekeningAntarResponse(
    @SerializedName("data")
    val data: CekRekeningAntarData?,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Int
)