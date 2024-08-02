package com.finsera.ui.fragments.info.mutasi.uistate

import com.finsera.domain.model.Mutasi

data class MutasiUiState (
    val isLoading: Boolean = false,
    val message: String? = null,
    val mutasi: List<Mutasi> = emptyList()
)