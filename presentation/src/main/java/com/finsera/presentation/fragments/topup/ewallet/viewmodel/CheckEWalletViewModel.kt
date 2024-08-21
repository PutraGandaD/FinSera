package com.finsera.presentation.fragments.topup.ewallet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finsera.common.utils.Resource
import com.finsera.common.utils.network.ConnectivityManager
import com.finsera.domain.usecase.transfer.ewallet.CekEWalletUseCase
import com.finsera.presentation.fragments.topup.ewallet.uistate.CheckEWalletUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CheckEWalletViewModel(
    private val connectivityManager: ConnectivityManager,
    private val cekEWalletUseCase: CekEWalletUseCase
) : ViewModel() {

    private val _cekEWalletUiState = MutableStateFlow(CheckEWalletUiState())
    val cekEWalletUiState = _cekEWalletUiState.asStateFlow()

    fun cekEWallet(
        eWalletId: Int, eWalletAccountNum: String
    ) {
        viewModelScope.launch {
            if (connectivityManager.hasInternetConnection()) {
                cekEWalletUseCase.invoke(
                    eWalletId, eWalletAccountNum
                ).collectLatest { result ->
                    when (result) {
                        is Resource.Loading -> {
                            _cekEWalletUiState.value = CheckEWalletUiState(
                                isLoading = true,
                                message = null,
                                isValid = false,
                                data = null
                            )
                        }

                        is Resource.Success -> {
                            _cekEWalletUiState.value = CheckEWalletUiState(
                                isLoading = false,
                                message = result.message,
                                isValid = true,
                                data = result.data
                            )
                        }

                        is Resource.Error -> {
                            _cekEWalletUiState.value = CheckEWalletUiState(
                                isLoading = false,
                                message = result.message,
                                isValid = false,
                                data = null
                            )
                        }
                    }
                }
            } else {
                _cekEWalletUiState.update { uiState ->
                    uiState.copy(
                        isLoading = false,
                        message = "Tidak ada koneksi internet.",
                        isValid = false,
                        data = null
                    )
                }
            }
        }
    }

    fun resetState() {
        _cekEWalletUiState.value = CheckEWalletUiState()
    }


    fun messageShown() {
        _cekEWalletUiState.update { currentUiState ->
            currentUiState.copy(message = null)
        }
    }

}