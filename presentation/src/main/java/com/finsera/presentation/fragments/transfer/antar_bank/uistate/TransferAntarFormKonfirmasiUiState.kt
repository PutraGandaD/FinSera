package com.finsera.presentation.fragments.transfer.antar_bank.uistate

import com.finsera.domain.model.TransferAntar

data class TransferAntarFormKonfirmasiUiState (
    val isLoading: Boolean = false,
    val message: String? = null,
    val isSuccess: Boolean = false,
    val data: TransferAntar? = null
)