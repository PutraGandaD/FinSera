package com.finsera.data.source.remote.response.ewallet

import com.google.gson.annotations.SerializedName

data class TransferEWalletResponse(

	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("data")
	val data: TransferEWalletData? = null,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)

data class TransferEWalletData(

	@field:SerializedName("feeAdmin")
	val feeAdmin: String? = null,

	@field:SerializedName("note")
	val note: String? = null,

	@field:SerializedName("accountSender")
	val accountSender: String? = null,

	@field:SerializedName("transactionNum")
	val transactionNum: String? = null,

	@field:SerializedName("ewalletName")
	val ewalletName: String? = null,

	@field:SerializedName("nominal")
	val nominal: String? = null,

	@field:SerializedName("nameSender")
	val nameSender: String? = null,

	@field:SerializedName("ewalletAccountName")
	val ewalletAccountName: String? = null,

	@field:SerializedName("transactionDate")
	val transactionDate: String? = null,

	@field:SerializedName("ewalletAccount")
	val ewalletAccount: String? = null
)
