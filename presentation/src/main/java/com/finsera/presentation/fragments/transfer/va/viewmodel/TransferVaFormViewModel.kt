package com.finsera.presentation.fragments.transfer.va.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finsera.common.utils.Resource
import com.finsera.common.utils.network.ConnectivityManager
import com.finsera.domain.usecase.transfer.virtual_account.TransferVaUseCase
import com.finsera.presentation.fragments.transfer.va.uistate.TransferVaFormConfirmUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TransferVaFormViewModel(
    private val connectivityManager: ConnectivityManager,
    private val transferVaUseCase: TransferVaUseCase
) : ViewModel(){

    private val _transferVaUiState = MutableStateFlow(TransferVaFormConfirmUiState())
    val transferVaUiState = _transferVaUiState.asStateFlow()


    fun transferVa(vaAccountNum: String, pin: String){
        viewModelScope.launch {
            if(connectivityManager.hasInternetConnection()){
                transferVaUseCase.invoke(vaAccountNum,pin).collectLatest { result ->
                    when(result){
                        is Resource.Loading -> {
                            _transferVaUiState.value = TransferVaFormConfirmUiState(
                                isLoading = true,
                                message = null,
                                isSuccess = false,
                                data = null
                            )
                        }

                        is Resource.Success -> {
                            _transferVaUiState.value = TransferVaFormConfirmUiState(
                                isLoading = false,
                                message = result.message,
                                isSuccess = true,
                                data = result.data
                            )
                        }

                        is Resource.Error -> {
                            _transferVaUiState.value = TransferVaFormConfirmUiState(
                                isLoading = false,
                                message = result.message,
                                isSuccess = false,
                                data = null
                            )
                        }
                    }
                }
            }else{
                _transferVaUiState.value = TransferVaFormConfirmUiState(
                    isLoading = false,
                    message = "No Internet Connection",
                    isSuccess = false,
                    data = null
                )
            }
        }
    }

    fun resetState(){
        _transferVaUiState.update { currentUiState ->
            currentUiState.copy(
                isLoading = false,
                message = null,
                isSuccess = false,
                data = null
            )
        }
    }

    fun messageShown() {
        _transferVaUiState.update { currentUiState ->
            currentUiState.copy(message = null)
        }
    }
}