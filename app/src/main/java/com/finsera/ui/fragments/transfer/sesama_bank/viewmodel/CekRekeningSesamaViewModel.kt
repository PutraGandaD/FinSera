package com.finsera.ui.fragments.transfer.sesama_bank.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.TYPE_ETHERNET
import android.net.ConnectivityManager.TYPE_WIFI
import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_ETHERNET
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import android.os.Build
import android.provider.ContactsContract.CommonDataKinds.Email.TYPE_MOBILE
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finsera.MainApplication
import com.finsera.common.utils.Resource
import com.finsera.domain.usecase.transfer_sesama.CekRekeningSesamaUseCase
import com.finsera.ui.fragments.transfer.sesama_bank.uistate.CekRekeningSesamaUiState
import com.finsera.ui.fragments.transfer.sesama_bank.uistate.TransferSesamaBankUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CekRekeningSesamaViewModel(
    app: Application,
    private val cekRekeningSesamaUseCase: CekRekeningSesamaUseCase
) : AndroidViewModel(app) {
    private val _cekRekeningSesamaUiState = MutableStateFlow(CekRekeningSesamaUiState())
    val cekRekeningSesamaUiState = _cekRekeningSesamaUiState.asStateFlow()

    fun cekRekeningSesama(norek: String) {
        viewModelScope.launch {
            if(hasInternetConnection()) {
                cekRekeningSesamaUseCase.invoke(norek).collectLatest { result ->
                    when(result) {
                        is Resource.Loading -> {
                            _cekRekeningSesamaUiState.update { uiState ->
                                uiState.copy(isLoading = true, message = null, isValid = false)
                            }
                        }

                        is Resource.Success -> {
                            _cekRekeningSesamaUiState.update { uiState ->
                                uiState.copy(isLoading = true, message = result.message, isValid = true)
                            }
                        }

                        is Resource.Error -> {
                            _cekRekeningSesamaUiState.update { uiState ->
                                uiState.copy(isLoading = false, message = result.message, isValid = false)
                            }
                        }
                    }
                }
            } else {
                _cekRekeningSesamaUiState.update { uiState ->
                    uiState.copy(
                        isLoading = false,
                        message = "Tidak ada koneksi internet.",
                        isValid = false
                    )
                }
            }
        }
    }

    fun messageShown() {
        _cekRekeningSesamaUiState.update { currentUiState ->
            currentUiState.copy(message = null)
        }
    }

    fun redirectedToKonfirmasiForm() {
        _cekRekeningSesamaUiState.update { currentUiState ->
            currentUiState.copy(isLoading = false, isValid = false)
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<MainApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when(type) {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}