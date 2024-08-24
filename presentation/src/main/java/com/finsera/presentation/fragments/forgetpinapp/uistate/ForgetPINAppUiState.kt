package com.finsera.presentation.fragments.forgetpinapp.uistate

data class ForgetPINAppUiState(
    val message: String? = null,
    val isSuccess: Boolean = false,
    val isLoading: Boolean = false,
    val isFailed: Boolean = false,
)
