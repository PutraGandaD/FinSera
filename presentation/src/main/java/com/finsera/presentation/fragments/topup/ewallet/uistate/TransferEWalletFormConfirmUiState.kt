package com.finsera.presentation.fragments.topup.ewallet.uistate

import com.finsera.domain.model.TransferEWallet

data class TransferEWalletFormConfirmUiState(
    val isLoading: Boolean = false,
    val message: String? = null,
    val isSuccess: Boolean = false,
    val data: TransferEWallet? = null
)
