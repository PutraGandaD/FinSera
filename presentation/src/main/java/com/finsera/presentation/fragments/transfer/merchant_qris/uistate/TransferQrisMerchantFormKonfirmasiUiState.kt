package com.finsera.presentation.fragments.transfer.merchant_qris.uistate

import com.finsera.domain.model.TransferQrisMerchant

data class TransferQrisMerchantFormKonfirmasiUiState(
    val isLoading: Boolean = false,
    val message: String? = null,
    val isSuccess: Boolean = false,
    val data: TransferQrisMerchant?  = null
)
