package com.finsera.data.source.remote.response.relogin

import com.google.gson.annotations.SerializedName

data class ReloginResponse(

    @field:SerializedName("data")
    val data: DataRelogin,

    @field:SerializedName("message")
    val message: String
)

data class DataRelogin(

    @field:SerializedName("username")
    val username: String,

    @field:SerializedName("status")
    val status: String
)
