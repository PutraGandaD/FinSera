package com.finsera.presentation.fragments.transfer.sesama_bank.uistate

import com.finsera.domain.model.TransferSesama

data class TransferSesamaFormKonfirmasiUiState(
    val isLoading: Boolean = false,
    val message: String? = null,
    val isSuccess: Boolean = false,
    val data: TransferSesama? = null
)