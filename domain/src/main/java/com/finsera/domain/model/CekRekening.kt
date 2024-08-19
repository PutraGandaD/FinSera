package com.finsera.domain.model

data class CekRekening(
    val accountnumRecipient: String?,
    val recipientName: String?,
    val idBank: Int?,
    val namaBank: String?,
    val message: String
)
