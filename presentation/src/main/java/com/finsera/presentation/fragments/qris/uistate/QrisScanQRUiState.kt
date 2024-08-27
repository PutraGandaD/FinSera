package com.finsera.presentation.fragments.qris.uistate

import com.finsera.domain.model.CekRekening

data class QrisScanQRUiState (
    val isValidFinsera: Boolean = false,
    val dataRekeningFinsera: CekRekening? = null,
    val isLoading: Boolean = false,
    val message: String? = null
)