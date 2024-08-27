package com.finsera.presentation.fragments.transfer.antar_bank.uistate

import com.finsera.domain.model.Bank

data class ListBankUiState(
    val data: List<Bank>? = null,
    val message: String? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false
)