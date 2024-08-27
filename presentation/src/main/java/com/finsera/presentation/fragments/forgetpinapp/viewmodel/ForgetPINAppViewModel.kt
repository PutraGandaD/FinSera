package com.finsera.presentation.fragments.forgetpinapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finsera.common.utils.Resource
import com.finsera.common.utils.network.ConnectivityManager
import com.finsera.domain.usecase.auth.ChangeAppPinForgetUseCase
import com.finsera.presentation.fragments.forgetpinapp.uistate.ForgetPINAppUiState
import com.finsera.presentation.fragments.ubahmpin.uistate.UbahMPINUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ForgetPINAppViewModel(
    private val connectivityManager: ConnectivityManager,
    private val changeAppPinForgetUseCase: ChangeAppPinForgetUseCase,
) : ViewModel() {
    private val _forgetPINAppUiState = MutableStateFlow(ForgetPINAppUiState())
    val forgetPINAppUiState = _forgetPINAppUiState.asStateFlow()

    fun changePin(newPin: String, confirmNewPin: String, accountPassword: String) = viewModelScope.launch {
        if(connectivityManager.hasInternetConnection()) {
            changeAppPinForgetUseCase.invoke(newPin, confirmNewPin, accountPassword).collectLatest { result ->
                when(result) {
                    is Resource.Loading -> {
                        _forgetPINAppUiState.update { uiState ->
                            uiState.copy(message = null, isSuccess = false, isLoading = true, isFailed = false)
                        }
                    }

                    is Resource.Error -> {
                        _forgetPINAppUiState.update { uiState ->
                            uiState.copy(message = result.message, isSuccess = false, isLoading = false, isFailed = true)
                        }
                    }

                    is Resource.Success -> {
                        _forgetPINAppUiState.update { uiState ->
                            uiState.copy(message = result.data, isSuccess = true, isLoading = true, isFailed = false)
                        }
                    }
                }
            }
        } else {
            _forgetPINAppUiState.update { uiState ->
                uiState.copy(message = "Tidak ada koneksi internet", isSuccess = false, isLoading = false, isFailed = false)
            }
        }

    }

    fun messageShown() = viewModelScope.launch {
        _forgetPINAppUiState.update { uiState ->
            uiState.copy(message = null)
        }
    }


}