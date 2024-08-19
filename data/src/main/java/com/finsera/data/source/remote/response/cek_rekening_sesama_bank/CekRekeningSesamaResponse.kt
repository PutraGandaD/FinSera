package com.finsera.data.source.remote.response.cek_rekening_sesama_bank


import com.google.gson.annotations.SerializedName

data class CekRekeningSesamaResponse(
    @SerializedName("data")
    val data: CekRekeningSesamaData?,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Int
)