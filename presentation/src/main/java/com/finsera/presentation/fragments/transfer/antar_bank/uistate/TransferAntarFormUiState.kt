package com.finsera.presentation.fragments.transfer.antar_bank.uistate

import com.finsera.domain.model.Saldo

data class TransferAntarFormUiState(
    val dataSaldo: Saldo? = null,
    val isDataSaldoLoading: Boolean = false,
    val isDataSaldoReady: Boolean = false,
    val hasInternet: Boolean = true,
    val message: String? = null
)
