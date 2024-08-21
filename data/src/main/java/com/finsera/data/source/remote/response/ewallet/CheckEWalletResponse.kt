package com.finsera.data.source.remote.response.ewallet

import com.google.gson.annotations.SerializedName

data class CheckEWalletResponse(

	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("data")
	val data: CheckEWalletData? = null,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)

data class CheckEWalletData(

	@field:SerializedName("ewalletAccountId")
	val ewalletAccountId: Int? = null,

	@field:SerializedName("ewalletName")
	val ewalletName: String? = null,

	@field:SerializedName("ewalletAccountName")
	val ewalletAccountName: String? = null,

	@field:SerializedName("ewalletAccount")
	val ewalletAccount: String? = null
)
