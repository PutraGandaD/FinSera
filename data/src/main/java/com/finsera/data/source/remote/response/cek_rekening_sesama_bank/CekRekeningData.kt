package com.finsera.data.source.remote.response.cek_rekening_sesama_bank


import com.google.gson.annotations.SerializedName

data class CekRekeningData(
    @SerializedName("accountnum_recipient")
    val accountnumRecipient: String,
    @SerializedName("name_recipient")
    val nameRecipient: String
)