package com.finsera.data.source.remote.response.notifikasi

import com.google.gson.annotations.SerializedName

data class NotificationResponse(

	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("data")
	val data: List<DataNotif>? = null,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)

data class DataNotif(

	@field:SerializedName("createdDate")
	val createdDate: String? = null,

	@field:SerializedName("typeNotification")
	val typeNotification: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("tittle")
	val tittle: String? = null
)
