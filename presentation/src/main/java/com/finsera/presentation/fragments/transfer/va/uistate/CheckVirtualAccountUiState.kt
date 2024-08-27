package com.finsera.presentation.fragments.transfer.va.uistate

import com.finsera.domain.model.CekVa

data class CheckVirtualAccountUiState(
    val isLoading: Boolean = false,
    val data: CekVa? = null,
    val message: String? = null,
    val isValid: Boolean = false
)
