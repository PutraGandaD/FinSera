package com.finsera.data.source.remote.response.cek_rekening_sesama_bank


import com.google.gson.annotations.SerializedName

data class CekRekeningResponse(
    @SerializedName("data")
    val data: CekRekeningData?,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Int
)