package com.finsera.presentation.fragments.profil.viewmodel

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finsera.common.utils.Resource
import com.finsera.common.utils.network.ConnectivityManager
import com.finsera.domain.usecase.auth.GetProfilingUserUseCase
import com.finsera.presentation.fragments.profil.uistate.ProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val connectivityManager: ConnectivityManager,
    private val profilingUserUseCase: GetProfilingUserUseCase
) : ViewModel() {
    private val _profileUiState = MutableStateFlow(ProfileUiState())
    val profileUiState = _profileUiState.asStateFlow()

    init {
        getProfileInfo()
    }

    private fun getProfileInfo() = viewModelScope.launch {
        if(connectivityManager.hasInternetConnection()) {
            profilingUserUseCase.invoke().collectLatest { result ->
                when(result) {
                    is Resource.Loading -> {
                        _profileUiState.update { uiState ->
                            uiState.copy(isLoading = true, isSuccess = false, data = null, message = null)
                        }
                    }

                    is Resource.Success -> {
                        _profileUiState.update { uiState ->
                            uiState.copy(isLoading = false, isSuccess = true, data = result.data, message = null)
                        }
                    }

                    is Resource.Error -> {
                        _profileUiState.update { uiState ->
                            uiState.copy(isLoading = false, isSuccess = false, data = null, message = result.message)
                        }
                    }
                }
            }
        } else {
            _profileUiState.update { uiState ->
                uiState.copy(isLoading = false, isSuccess = false, data = null, message = "Tidak ada koneksi internet")
            }
        }
    }

    fun messageShown() {
        _profileUiState.update { uiState ->
            uiState.copy(message = null)
        }
    }
}