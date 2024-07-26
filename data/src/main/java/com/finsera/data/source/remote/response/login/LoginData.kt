package com.finsera.data.source.remote.response.login


import com.google.gson.annotations.SerializedName

data class LoginData(
    @SerializedName("status")
    val status: String,
    @SerializedName("token")
    val token: String,
    @SerializedName("username")
    val username: String
)