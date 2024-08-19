package com.finsera.presentation.fragments.transfer.va.uistate

import com.finsera.domain.model.TransferVa

data class TransferVaFormConfirmUiState(
    val isLoading: Boolean = false,
    val message: String? = null,
    val isSuccess: Boolean = false,
    val data: TransferVa? = null
)
