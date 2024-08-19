package com.finsera.data.source.remote.response.cek_rekening_antar_bank


import com.google.gson.annotations.SerializedName

data class CekRekeningAntarData(
    @SerializedName("accountnum_recipient")
    val accountnumRecipient: String,
    @SerializedName("bank_id")
    val bankId: Int,
    @SerializedName("bank_name")
    val bankName: String,
    @SerializedName("name_recipient")
    val nameRecipient: String
)