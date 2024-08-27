package com.finsera.presentation.fragments.ubahmpin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finsera.common.utils.Resource
import com.finsera.domain.usecase.auth.ChangeAppPinUseCase
import com.finsera.presentation.fragments.ubahmpin.uistate.UbahMPINUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UbahMPINViewModel(
    private val changeAppPinUseCase: ChangeAppPinUseCase
): ViewModel() {
    private val _changePinUiState = MutableStateFlow(UbahMPINUiState())
    val changePinUiState = _changePinUiState.asStateFlow()

    fun changePin(oldPin: String, newPin: String, confirmPin: String) = viewModelScope.launch {
        changeAppPinUseCase.invoke(oldPin, newPin, confirmPin).collectLatest { result ->
            when(result) {
                is Resource.Loading -> {
                    _changePinUiState.update { uiState ->
                        uiState.copy(message = null, isSuccess = false, isLoading = true, isFailed = false)
                    }
                }

                is Resource.Error -> {
                    _changePinUiState.update { uiState ->
                        uiState.copy(message = result.message, isSuccess = false, isLoading = false, isFailed = true)
                    }
                }

                is Resource.Success -> {
                    _changePinUiState.update { uiState ->
                        uiState.copy(message = result.data, isSuccess = true, isLoading = true, isFailed = false)
                    }
                }
            }
        }
    }

    fun messageShown() = viewModelScope.launch {
        _changePinUiState.update { uiState ->
            uiState.copy(message = null)
        }
    }
}