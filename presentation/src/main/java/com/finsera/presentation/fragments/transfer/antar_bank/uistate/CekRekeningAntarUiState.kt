package com.finsera.presentation.fragments.transfer.antar_bank.uistate

import com.finsera.domain.model.CekRekening

data class CekRekeningAntarUiState(
    val isLoading: Boolean = false,
    val data: CekRekening? = null,
    val message: String? = null,
    val isValid: Boolean = false
)