package com.finsera.presentation.fragments.transfer.merchant_qris.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finsera.common.utils.Resource
import com.finsera.common.utils.network.ConnectivityManager
import com.finsera.domain.usecase.auth.GetUserInfoUseCase
import com.finsera.domain.usecase.qris.TransferQrisMerchantUseCase
import com.finsera.presentation.fragments.transfer.merchant_qris.uistate.TransferQrisMerchantFormKonfirmasiUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TransferQrisMerchantViewModel(
    private val connectivityManager : ConnectivityManager,
    private val transferQrisMerchantUseCase: TransferQrisMerchantUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase
) : ViewModel() {
    private val _transferQrisMerchantKonfirmasiUiState = MutableStateFlow(TransferQrisMerchantFormKonfirmasiUiState())
    val transferQrisMerchantKonfirmasiUiState = _transferQrisMerchantKonfirmasiUiState.asStateFlow()

    private var _userInfo : Pair<String, String>? = null
    val userInfo : Pair<String, String>?
        get() = _userInfo

    init {
        getUserInfo()
    }

    fun transferQrisMerchant(merchantNo: String, merchantName: String, nominal: Double, mpin: String) {
        viewModelScope.launch {
            if(connectivityManager.hasInternetConnection()) {
                transferQrisMerchantUseCase.invoke(merchantNo, merchantName, nominal, mpin).collectLatest { result ->
                    when(result) {
                        is Resource.Loading -> {
                            _transferQrisMerchantKonfirmasiUiState.update { uiState ->
                                uiState.copy(isSuccess = false, isLoading = true, data = null, message = null)
                            }
                        }

                        is Resource.Success -> {
                            _transferQrisMerchantKonfirmasiUiState.update { uiState ->
                                uiState.copy(isSuccess = true, isLoading = false, data = result.data, message = null)
                            }
                        }

                        is Resource.Error -> {
                            _transferQrisMerchantKonfirmasiUiState.update { uiState ->
                                uiState.copy(isSuccess = false, isLoading = false, data = null, message = result.message)
                            }
                        }
                    }
                }
            } else {
                _transferQrisMerchantKonfirmasiUiState.update { uiState ->
                    uiState.copy(isSuccess = false, isLoading = false, data = null, message = "Tidak ada koneksi internet.")
                }
            }
        }
    }

    fun messageFormKonfirmasiShown() {
        _transferQrisMerchantKonfirmasiUiState.update { currentUiState ->
            currentUiState.copy(message = null)
        }
    }

    fun transferQrisMerchantBerhasil() {
        _transferQrisMerchantKonfirmasiUiState.update { currentUiState ->
            currentUiState.copy(isLoading = false, isSuccess = false, message = null, data = null)
        }
    }

    private fun getUserInfo() {
        _userInfo = getUserInfoUseCase.invoke()
    }
}