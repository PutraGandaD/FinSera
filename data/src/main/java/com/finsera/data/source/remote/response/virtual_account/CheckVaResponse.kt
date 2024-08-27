package com.finsera.data.source.remote.response.virtual_account

import com.google.gson.annotations.SerializedName

data class CheckVaResponse(
	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("status")
	val status: Boolean,

	@field:SerializedName("data")
	val data: CheckVaData? = null,

	@field:SerializedName("message")
	val message: String
)

data class CheckVaData(

	@field:SerializedName("accountNum")
	val accountNum: String,

	@field:SerializedName("nominal")
	val nominal: Int,

	@field:SerializedName("accountName")
	val accountName: String
)
