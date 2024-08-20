package com.finsera.presentation.fragments.topup.ewallet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finsera.common.utils.Resource
import com.finsera.common.utils.network.ConnectivityManager
import com.finsera.domain.usecase.transfer.ewallet.CariDaftarEWalletTersimpanUseCase
import com.finsera.domain.usecase.transfer.ewallet.GetDaftarTersimpanEWalletUseCase
import com.finsera.domain.usecase.transfer.ewallet.TambahDaftarTersimpanEWalletUseCase
import com.finsera.domain.usecase.transfer.ewallet.TransferEWalletUseCase
import com.finsera.presentation.fragments.topup.ewallet.uistate.TransferEWalletFormConfirmUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TransferEWalletViewModel(
    private val connectivityManager: ConnectivityManager,
    private val transferEWalletUseCase: TransferEWalletUseCase,
    private val getDaftarTersimpanEwalletUseCase: GetDaftarTersimpanEWalletUseCase,
    private val tambahDaftarTersimpanEwalletUseCase: TambahDaftarTersimpanEWalletUseCase,
    private val cariDaftarTersimpanEwalletUseCase: CariDaftarEWalletTersimpanUseCase
) : ViewModel() {

    private val _transferEWalletUiState = MutableStateFlow(TransferEWalletFormConfirmUiState())
    val transferEWalletUiState = _transferEWalletUiState.asStateFlow()

    fun transferEWallet(
        eWalletId: Int,
        eWalletAccountNum: String,
        nominal: Double,
        note: String,
        pin: String
    ) {
        viewModelScope.launch {
            if (connectivityManager.hasInternetConnection()) {
                transferEWalletUseCase.invoke(
                    eWalletId,
                    eWalletAccountNum,
                    nominal,
                    note,
                    pin
                ).collectLatest { result ->
                    when (result) {
                        is Resource.Loading -> {
                            _transferEWalletUiState.value = TransferEWalletFormConfirmUiState(
                                isLoading = true,
                                message = null,
                                isSuccess = false,
                                data = null
                            )
                        }

                        is Resource.Success -> {
                            _transferEWalletUiState.value = TransferEWalletFormConfirmUiState(
                                isLoading = false,
                                message = result.message,
                                isSuccess = true,
                                data = result.data
                            )
                        }

                        is Resource.Error -> {
                            _transferEWalletUiState.value = TransferEWalletFormConfirmUiState(
                                isLoading = false,
                                message = result.message,
                                isSuccess = false,
                                data = null
                            )
                        }
                    }
                }
            } else {
                _transferEWalletUiState.value = TransferEWalletFormConfirmUiState(
                    isLoading = false,
                    message = "No Internet Connection",
                    isSuccess = false,
                    data = null
                )
            }
        }
    }

    fun resetState() {
        _transferEWalletUiState.update { currentUiState ->
            currentUiState.copy(
                isLoading = false,
                message = null,
                isSuccess = false,
                data = null
            )
        }
    }

    fun messageShown() {
        _transferEWalletUiState.update { currentUiState ->
            currentUiState.copy(message = null)
        }
    }
}