package com.finsera.ui.fragments.transfer.sesama_bank.uistate

data class CekRekeningSesamaUiState(
    val isLoading: Boolean = false,
    val message: String? = null,
    val isValid: Boolean = false
)