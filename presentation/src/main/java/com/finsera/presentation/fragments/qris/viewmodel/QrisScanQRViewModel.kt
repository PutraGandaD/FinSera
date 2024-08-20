package com.finsera.presentation.fragments.qris.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finsera.common.utils.Resource
import com.finsera.common.utils.network.ConnectivityManager
import com.finsera.domain.usecase.transfer.sesama_bank.CekRekeningSesamaUseCase
import com.finsera.presentation.fragments.qris.uistate.QrisScanQRUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class QrisScanQRViewModel(
    private val connectivityManager: ConnectivityManager,
    private val cekRekeningSesamaUseCase: CekRekeningSesamaUseCase
) : ViewModel() {
    private val _qrisScanUiState = MutableStateFlow(QrisScanQRUiState())
    val qrisScanQRUiState = _qrisScanUiState.asStateFlow()

    fun cekRekeningSesama(norek: String) {
        viewModelScope.launch {
            if (connectivityManager.hasInternetConnection()) {
                cekRekeningSesamaUseCase.invoke(norek).collectLatest { result ->
                    when (result) {
                        is Resource.Loading -> {
                            _qrisScanUiState.update { uiState ->
                                uiState.copy(
                                    isLoading = true,
                                    dataRekeningFinsera = result.data,
                                    isValidFinsera = false,
                                    message = null
                                )
                            }
                        }

                        is Resource.Success -> {
                            _qrisScanUiState.update { uiState ->
                                uiState.copy(
                                    isLoading = false,
                                    dataRekeningFinsera = result.data,
                                    isValidFinsera = true,
                                    message = null
                                )
                            }
                        }

                        is Resource.Error -> {
                            _qrisScanUiState.update { uiState ->
                                uiState.copy(
                                    isLoading = false,
                                    dataRekeningFinsera = result.data,
                                    isValidFinsera = false,
                                    message = "QRIS Tidak Valid"
                                )
                            }
                        }
                    }
                }
            } else {
                _qrisScanUiState.update { uiState ->
                    uiState.copy(
                        isLoading = false,
                        message = "Tidak ada koneksi internet.",
                        dataRekeningFinsera = null,
                        isValidFinsera = false
                    )
                }
            }
        }
    }

    fun messageShown() {
        _qrisScanUiState.update { currentUiState ->
            currentUiState.copy(message = null)
        }
    }

    fun resetUiState() {
        _qrisScanUiState.update { currentUiState ->
            currentUiState.copy(
                isLoading = false,
                message = null,
                dataRekeningFinsera = null,
                isValidFinsera = false
            )
        }
    }
}