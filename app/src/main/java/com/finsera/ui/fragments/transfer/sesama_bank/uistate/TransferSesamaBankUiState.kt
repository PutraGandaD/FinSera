package com.finsera.ui.fragments.transfer.sesama_bank.uistate

data class TransferSesamaBankUiState(
    val isLoading: Boolean = false,
    val message: String? = null,
    val isSuccess: Boolean = false
)