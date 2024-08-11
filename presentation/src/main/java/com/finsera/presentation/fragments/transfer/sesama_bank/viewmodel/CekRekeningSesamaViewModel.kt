package com.finsera.presentation.fragments.transfer.sesama_bank.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finsera.common.utils.Resource
import com.finsera.common.utils.network.ConnectivityManager
import com.finsera.domain.usecase.transfer.sesama_bank.CekRekeningSesamaUseCase
import com.finsera.presentation.fragments.transfer.sesama_bank.uistate.CekRekeningSesamaUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CekRekeningSesamaViewModel(
    private val connectivityManager: ConnectivityManager,
    private val cekRekeningSesamaUseCase: CekRekeningSesamaUseCase
) : ViewModel() {
    private val _cekRekeningSesamaUiState = MutableStateFlow(CekRekeningSesamaUiState())
    val cekRekeningSesamaUiState = _cekRekeningSesamaUiState.asStateFlow()

    fun cekRekeningSesama(norek: String) {
        viewModelScope.launch {
            if(connectivityManager.hasInternetConnection()) {
                cekRekeningSesamaUseCase.invoke(norek).collectLatest { result ->
                    when(result) {
                        is Resource.Loading -> {
                            _cekRekeningSesamaUiState.update { uiState ->
                                uiState.copy(isLoading = true, message = null, isValid = false, data = null)
                            }
                        }

                        is Resource.Success -> {
                            _cekRekeningSesamaUiState.update { uiState ->
                                uiState.copy(isLoading = false, message = result.message, isValid = true, data = result.data)
                            }
                        }

                        is Resource.Error -> {
                            _cekRekeningSesamaUiState.update { uiState ->
                                uiState.copy(isLoading = false, message = result.message, isValid = false, data = null)
                            }
                        }
                    }
                }
            } else {
                _cekRekeningSesamaUiState.update { uiState ->
                    uiState.copy(
                        isLoading = false,
                        message = "Tidak ada koneksi internet.",
                        isValid = false
                    )
                }
            }
        }
    }

    fun messageShown() {
        _cekRekeningSesamaUiState.update { currentUiState ->
            currentUiState.copy(message = null)
        }
    }

    fun redirectedToKonfirmasiForm() {
        _cekRekeningSesamaUiState.update { currentUiState ->
            currentUiState.copy(isLoading = false, isValid = false)
        }
    }
}