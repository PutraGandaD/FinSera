package com.finsera.ui.fragments.transfer.sesama_bank.uistate

import com.finsera.domain.model.CekRekening

data class CekRekeningSesamaUiState(
    val isLoading: Boolean = false,
    val data: CekRekening? = null,
    val message: String? = null,
    val isValid: Boolean = false
)