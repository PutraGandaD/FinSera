package com.finsera.data.source.remote.response.profiling


import com.google.gson.annotations.SerializedName

data class ProfilingData(
    @SerializedName("address")
    val address: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("idCustomer")
    val idCustomer: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("username")
    val username: String
)