package com.finsera.data.source.remote.response.profiling


import com.google.gson.annotations.SerializedName

data class ProfilingResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val data: ProfilingData,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
)