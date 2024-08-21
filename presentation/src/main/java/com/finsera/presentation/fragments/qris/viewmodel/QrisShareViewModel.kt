package com.finsera.presentation.fragments.qris.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finsera.common.utils.Resource
import com.finsera.common.utils.network.ConnectivityManager
import com.finsera.domain.usecase.qris.QRShareUseCase
import com.finsera.presentation.fragments.qris.uistate.QrisShareUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class QrisShareViewModel(
    private val connectivityManager: ConnectivityManager,
    private val shareQRShareUseCase: QRShareUseCase
) : ViewModel() {
    private val _shareQrUiState = MutableStateFlow(QrisShareUiState())
    val shareQrUiState = _shareQrUiState.asStateFlow()

    init {
        getShareQR()
    }

    fun getShareQR() = viewModelScope.launch {
        if(connectivityManager.hasInternetConnection()) {
            shareQRShareUseCase.invoke().collectLatest { result ->
                when(result) {
                    is Resource.Loading -> {
                        _shareQrUiState.update { uiState ->
                            uiState.copy(isSuccess = false, isLoading = true, message = null, data = null)
                        }
                    }

                    is Resource.Success -> {
                        _shareQrUiState.update { uiState ->
                            uiState.copy(isSuccess = true, isLoading = false, message = null, data = result.data)
                        }
                    }

                    is Resource.Error -> {
                        _shareQrUiState.update { uiState ->
                            uiState.copy(isSuccess = false, isLoading = false, message = result.message, data = null)
                        }
                    }
                }
            }
        } else {
            _shareQrUiState.update { uiState ->
                uiState.copy(isSuccess = false, isLoading = false, message = "Tidak ada koneksi internet", data = null)
            }
        }
    }

    fun messageShown() {
        _shareQrUiState.update { currentUiState ->
            currentUiState.copy(message = null)
        }
    }

    fun resetUiState() {
        _shareQrUiState.update { currentUiState ->
            currentUiState.copy(
                isLoading = false,
                message = null,
                isSuccess = false,
                data = null
            )
        }
    }
}