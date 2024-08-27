package com.finsera.data.source.remote.response.virtual_account

import com.google.gson.annotations.SerializedName

data class TransferVaResponse(
	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("status")
	val status: Boolean,

	@field:SerializedName("data")
	val data: TransferVaData? = null,

	@field:SerializedName("message")
	val message: String
)

data class TransferVaData(

	@field:SerializedName("transactionNum")
	val transactionNum: String,

	@field:SerializedName("nominal")
	val nominal: String,

	@field:SerializedName("recipientName")
	val recipientName: String,

	@field:SerializedName("adminFee")
	val adminFee: String,

	@field:SerializedName("transactionDate")
	val transactionDate: String,

	@field:SerializedName("type")
	val type: String,

	@field:SerializedName("recipientVirtualAccountNum")
	val recipientVirtualAccountNum: String
)
