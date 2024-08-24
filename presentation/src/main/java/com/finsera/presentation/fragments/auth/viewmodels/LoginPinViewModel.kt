package com.finsera.presentation.fragments.auth.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finsera.common.utils.Resource
import com.finsera.common.utils.network.ConnectivityManager
import com.finsera.domain.usecase.auth.CreateAppPinUseCase
import com.finsera.domain.usecase.auth.LoginAppPinUserUseCase
import com.finsera.domain.usecase.auth.LogoutUserUseCase
import com.finsera.presentation.fragments.auth.uistate.LoginPinUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginPinViewModel(
    private val connectivityManager: ConnectivityManager,
    private val loginAppPinUserUseCase: LoginAppPinUserUseCase,
    private val createAppPinUseCase: CreateAppPinUseCase,
    private val logoutUserUseCase: LogoutUserUseCase
) : ViewModel() {
    private val _loginPinScreenUIState = MutableStateFlow(LoginPinUiState())
    val loginPinScreenUIState = _loginPinScreenUIState.asStateFlow()

    fun loginWithMpin(pin: String) {
        viewModelScope.launch {
            if(connectivityManager.hasInternetConnection()) {
                loginAppPinUserUseCase.invoke(pin).collectLatest { result ->
                    when(result) {
                        is Resource.Loading -> {
                            _loginPinScreenUIState.update { uiState ->
                                uiState.copy(isLoading = true, message = null, isPinCorrect = false)
                            }
                        }

                        is Resource.Success -> {
                            _loginPinScreenUIState.update { uiState ->
                                uiState.copy(isLoading = false, message = null, isPinCorrect = true)
                            }
                        }

                        is Resource.Error -> {
                            _loginPinScreenUIState.update { uiState ->
                                uiState.copy(isLoading = false, message = result.message, isPinCorrect = false)
                            }
                        }
                    }
                }
            } else {
                _loginPinScreenUIState.update { uiState ->
                    uiState.copy(
                        isLoading = false,
                        message = "Tidak ada Koneksi Internet",
                        isPinCorrect = false
                    )
                }
            }
        }
    }

    fun userMessageShown() {
        _loginPinScreenUIState.update { currentUiState ->
            currentUiState.copy(message = null)
        }
    }

    fun createAppPin(newPin: String, confirmNewPin: String) : Boolean {
        return createAppPinUseCase.invoke(newPin, confirmNewPin)
    }

    fun logoutFromAccount() {
        logoutUserUseCase.invoke()
    }
}