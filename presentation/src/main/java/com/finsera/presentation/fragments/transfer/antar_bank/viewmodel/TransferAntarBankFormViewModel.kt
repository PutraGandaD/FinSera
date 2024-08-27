package com.finsera.presentation.fragments.transfer.antar_bank.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finsera.common.utils.Resource
import com.finsera.common.utils.network.ConnectivityManager
import com.finsera.domain.usecase.infosaldo.InfoSaldoUseCase
import com.finsera.presentation.fragments.transfer.antar_bank.uistate.TransferAntarFormUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TransferAntarBankFormViewModel(
    private val connectivityManager: ConnectivityManager,
    private val getInfoSaldoUseCase: InfoSaldoUseCase
): ViewModel() {
    private val _transferAntarFormUiState = MutableStateFlow(TransferAntarFormUiState())
    val transferAntarFormUiState = _transferAntarFormUiState.asStateFlow()

    fun getInfoSaldoSaya() = viewModelScope.launch {
        if(connectivityManager.hasInternetConnection()) {
            getInfoSaldoUseCase.invoke().collectLatest { result ->
                when(result) {
                    is Resource.Loading -> {
                        _transferAntarFormUiState.update { uiState ->
                            uiState.copy(isDataSaldoLoading = true, isDataSaldoReady = false, message = null, dataSaldo = null, hasInternet = true)
                        }
                    }
                    is Resource.Success -> {
                        _transferAntarFormUiState.update { uiState ->
                            uiState.copy(isDataSaldoLoading = false, isDataSaldoReady = true, message = null, dataSaldo = result.data, hasInternet = true)
                        }
                    }
                    is Resource.Error -> {
                        _transferAntarFormUiState.update { uiState ->
                            uiState.copy(isDataSaldoLoading = false, isDataSaldoReady = false, message = result.message, dataSaldo = null, hasInternet = true)
                        }
                    }
                }
            }
        } else {
            _transferAntarFormUiState.update { uiState ->
                uiState.copy(isDataSaldoLoading = false, isDataSaldoReady = false, message = "Tidak ada koneksi internet.", dataSaldo = null, hasInternet = false)
            }
        }
    }

    fun messageFormShown() {
        _transferAntarFormUiState.update { currentUiState ->
            currentUiState.copy(message = null)
        }
    }
}