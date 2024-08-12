package com.finsera.presentation.fragments.transfer.sesama_bank.uistate

import com.finsera.domain.model.Saldo

data class TransferSesamaFormUiState(
    val dataSaldo: Saldo? = null,
    val isDataSaldoLoading: Boolean = false,
    val isDataSaldoReady: Boolean = false,
    val hasInternet: Boolean = true,
    val message: String? = null
)
