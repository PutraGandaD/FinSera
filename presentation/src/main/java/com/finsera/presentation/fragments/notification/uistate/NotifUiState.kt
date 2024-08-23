package com.finsera.presentation.fragments.notification.uistate

import com.finsera.domain.model.Notifikasi

data class NotifUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val isError: Boolean = false,
    val message: String? = null,
    val notif: List<Notifikasi>? = null
)
