package com.finsera.presentation.fragments.info.mutasi.uistate

import com.finsera.domain.model.Mutasi

data class MutasiUiState (
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val isError: Boolean = false,
    val message: String? = null,
    val mutasi: List<Mutasi>? = null
)