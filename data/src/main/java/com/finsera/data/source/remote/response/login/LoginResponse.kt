package com.finsera.data.source.remote.response.login

import com.google.gson.annotations.SerializedName


data class LoginResponse(

	@field:SerializedName("data")
	val data: DataLogin,

	@field:SerializedName("message")
	val message: String
)

data class DataLogin(

	@field:SerializedName("token")
	val token: String,

	@field:SerializedName("username")
	val username: String,

	@field:SerializedName("status")
	val status: String
)
