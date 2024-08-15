package com.finsera.presentation.fragments.transfer.va.uistate

import com.finsera.domain.model.CekVa

data class TransferVirtualAccountUiState(
    val isLoading: Boolean = false,
    val data: CekVa? = null,
    val message: String? = null,
    val isValid: Boolean = false
)
