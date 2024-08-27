package com.finsera.data.source.remote.response.mutasi


import com.google.gson.annotations.SerializedName

data class MutasiAmount(
    @SerializedName("amount")
    val amount: Double,
    @SerializedName("currency")
    val currency: String
)