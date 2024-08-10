package com.finsera.presentation.fragments.transfer.sesama_bank.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finsera.common.utils.Resource
import com.finsera.common.utils.network.ConnectivityManager
import com.finsera.domain.usecase.transfer_sesama.TransferSesamaBankUseCase
import com.finsera.presentation.fragments.transfer.sesama_bank.uistate.TransferSesamaBankUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TransferSesamaBankViewModel(
    private val connectivityManager: ConnectivityManager,
    private val transferSesamaBankUseCase: TransferSesamaBankUseCase
) : ViewModel() {
    private val _transferSesamaUiState = MutableStateFlow(TransferSesamaBankUiState())
    val transferSesamaUiState = _transferSesamaUiState.asStateFlow()

    fun transferSesama(norek: String, nominal: Double, note: String, mpin: String) {
        viewModelScope.launch {
            if(connectivityManager.hasInternetConnection()) {
                transferSesamaBankUseCase.invoke(norek, nominal, note, mpin).collectLatest { result ->
                    when(result) {
                        is Resource.Loading -> {
                            _transferSesamaUiState.update { uiState ->
                                uiState.copy(isLoading = true, message = null, isSuccess = false)
                            }
                        }

                        is Resource.Success -> {
                            _transferSesamaUiState.update { uiState ->
                                uiState.copy(isLoading = true, message = result.message, isSuccess = true)
                            }
                        }

                        is Resource.Error -> {
                            _transferSesamaUiState.update { uiState ->
                                uiState.copy(isLoading = false, message = result.message, isSuccess = false)
                            }
                        }
                    }
                }
            } else {
                _transferSesamaUiState.update { uiState ->
                    uiState.copy(
                        isLoading = false,
                        message = "Tidak ada koneksi internet.",
                        isSuccess = false
                    )
                }
            }
        }
    }

    fun messageShown() {
        _transferSesamaUiState.update { currentUiState ->
            currentUiState.copy(message = null)
        }
    }
}