package com.finsera.presentation.fragments.transfer.va.uistate

import com.finsera.domain.model.DaftarTersimpanSesama
import com.finsera.domain.model.DaftarTersimpanVa


data class TransferVaHomeUiState(
    val data : List<DaftarTersimpanVa> = emptyList()
)
