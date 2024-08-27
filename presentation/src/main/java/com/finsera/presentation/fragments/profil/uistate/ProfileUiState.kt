package com.finsera.presentation.fragments.profil.uistate

import com.finsera.domain.model.Profiling

data class ProfileUiState(
    val isLoading: Boolean = false,
    val data: Profiling? = null,
    val isSuccess: Boolean = false,
    val message: String? = null
)