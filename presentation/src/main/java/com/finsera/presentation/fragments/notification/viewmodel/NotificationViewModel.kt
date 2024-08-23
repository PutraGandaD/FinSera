package com.finsera.presentation.fragments.notification.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finsera.common.utils.Resource
import com.finsera.common.utils.network.ConnectivityManager
import com.finsera.domain.usecase.notifikasi.NotifikasiUseCase
import com.finsera.presentation.fragments.notification.uistate.NotifUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotificationViewModel(
    private val notifikasiUseCase: NotifikasiUseCase,
    private val connectivityManager: ConnectivityManager,
) : ViewModel(){

    private val _notifUiState = MutableStateFlow(NotifUiState())
    val notifUiState = _notifUiState.asStateFlow()

    init {
        getNotifikasi()
    }

    fun getNotifikasi(){
        viewModelScope.launch {
            if(connectivityManager.hasInternetConnection()){
                notifikasiUseCase.invoke().collectLatest { result ->
                    when(result){
                        is Resource.Loading -> {
                            _notifUiState.update { uiState ->
                                uiState.copy(isLoading = true, isSuccess = false, isError = false, message = null, notif= null)
                            }
                        }

                        is Resource.Success -> {
                            _notifUiState.update { uiState ->
                                uiState.copy(isLoading = false, isSuccess = true, isError = false, message = "Notifikasi berhasil dimuat", notif = result.data)
                            }
                        }

                        is Resource.Error -> {
                            _notifUiState.update { uiState ->
                                uiState.copy(isLoading = false, isSuccess = false, isError = true, message = result.message, notif = null)
                            }
                        }
                    }
                }
            }
        }
    }

    fun messageShown() {
        _notifUiState.update { uiState ->
            uiState.copy(message = null)
        }
    }

}