package com.finsera.data.source.remote.response.qris_share


import com.google.gson.annotations.SerializedName

data class QrisShareData(
    @SerializedName("accountNumber")
    val accountNumber: String,
    @SerializedName("username")
    val username: String
)