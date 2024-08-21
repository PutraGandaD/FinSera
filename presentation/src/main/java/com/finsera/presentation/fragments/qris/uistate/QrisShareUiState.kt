package com.finsera.presentation.fragments.qris.uistate

import com.finsera.domain.model.QRShare

data class QrisShareUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val message: String? = null,
    val data: QRShare? = null
)
