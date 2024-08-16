package com.finsera.presentation.fragments.transfer.va.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finsera.common.utils.Resource
import com.finsera.common.utils.network.ConnectivityManager
import com.finsera.domain.usecase.transfer.virtual_account.CekVirtualAccountUseCase
import com.finsera.presentation.fragments.transfer.va.uistate.ChcekVirtualAccountUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class CheckVaViewModel(
    private val connectivityManager: ConnectivityManager,
    private val cekVirtualAccountUseCase: CekVirtualAccountUseCase
) : ViewModel() {


    private val _cekVaUiState = MutableStateFlow(ChcekVirtualAccountUiState())
    val cekVaUiState = _cekVaUiState.asStateFlow()


    fun cekVirtualAccount(vaAccountNum: String) {
        viewModelScope.launch {
            if (connectivityManager.hasInternetConnection()) {
                cekVirtualAccountUseCase.invoke(vaAccountNum).collectLatest { result ->
                    when (result) {
                        is Resource.Loading -> {
                            _cekVaUiState.value = ChcekVirtualAccountUiState(
                                isLoading = true,
                                message = null,
                                isValid = false,
                                data = null
                            )
                        }

                        is Resource.Success -> {
                            _cekVaUiState.value = ChcekVirtualAccountUiState(
                                isLoading = false,
                                message = result.message,
                                isValid = true,
                                data = result.data
                            )
                        }

                        is Resource.Error -> {
                            _cekVaUiState.value = ChcekVirtualAccountUiState(
                                isLoading = false,
                                message = result.message,
                                isValid = false,
                                data = null
                            )
                        }
                    }
                }
            } else {
                _cekVaUiState.update { uiState ->
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
        _cekVaUiState.value = ChcekVirtualAccountUiState()
    }


    fun messageShown() {
        _cekVaUiState.update { currentUiState ->
            currentUiState.copy(message = null)
        }
    }
}