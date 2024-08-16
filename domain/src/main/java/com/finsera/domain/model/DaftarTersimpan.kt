package com.finsera.domain.model

data class DaftarTersimpan(
    val id: Int,
    val namaPemilikRekening: String,
    val noRekening: String,
    val nominal: Int?= null
)
