package com.finsera.presentation.fragments.transfer.merchant_qris.uistate

import com.finsera.domain.model.Saldo

data class TransferQrisMerchantFormUiState(
    val dataSaldo: Saldo? = null,
    val isDataSaldoLoading: Boolean = false,
    val isDataSaldoReady: Boolean = false,
    val hasInternet: Boolean = true,
    val message: String? = null
)
