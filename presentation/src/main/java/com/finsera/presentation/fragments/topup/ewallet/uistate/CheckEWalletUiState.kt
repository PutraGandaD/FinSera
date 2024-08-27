package com.finsera.presentation.fragments.topup.ewallet.uistate

import com.finsera.domain.model.CekEWallet

data class CheckEWalletUiState(
    val isLoading: Boolean = false,
    val data: CekEWallet? = null,
    val message: String? = null,
    val isValid: Boolean = false
)
