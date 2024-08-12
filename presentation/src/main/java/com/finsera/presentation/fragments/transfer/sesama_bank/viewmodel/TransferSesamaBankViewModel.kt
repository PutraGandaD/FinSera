package com.finsera.presentation.fragments.transfer.sesama_bank.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finsera.common.utils.Resource
import com.finsera.common.utils.network.ConnectivityManager
import com.finsera.domain.model.DaftarTersimpan
import com.finsera.domain.usecase.transfer.sesama_bank.CariDaftarTersimpanSesamaUseCase
import com.finsera.domain.usecase.transfer.sesama_bank.GetDaftarTersimpanSesamaUseCase
import com.finsera.domain.usecase.transfer.sesama_bank.TambahDaftarTersimpanSesamaUseCase
import com.finsera.domain.usecase.transfer.sesama_bank.TransferSesamaBankUseCase
import com.finsera.presentation.fragments.transfer.sesama_bank.uistate.TransferSesamaFormUiState
import com.finsera.presentation.fragments.transfer.sesama_bank.uistate.TransferSesamaHomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TransferSesamaBankViewModel(
    private val connectivityManager: ConnectivityManager,
    private val transferSesamaBankUseCase: TransferSesamaBankUseCase,
    private val tambahDaftarTersimpanSesamaUseCase: TambahDaftarTersimpanSesamaUseCase,
    private val getDaftarTersimpanSesamaUseCase: GetDaftarTersimpanSesamaUseCase,
    private val cariDaftarTersimpanSesamaUseCase: CariDaftarTersimpanSesamaUseCase
) : ViewModel() {
    private val _transferSesamaFormUiState = MutableStateFlow(TransferSesamaFormUiState())
    val transferSesamaFormUiState = _transferSesamaFormUiState.asStateFlow()

    private val _transferSesamaHomeUiState = MutableStateFlow(TransferSesamaHomeUiState())
    val transferSesamaHomeUiState = _transferSesamaHomeUiState.asStateFlow()

    init {
        getDaftarTersimpanSesama()
    }

    fun transferSesama(norek: String, nominal: Double, note: String, mpin: String) {
        viewModelScope.launch {
            if(connectivityManager.hasInternetConnection()) {
                transferSesamaBankUseCase.invoke(norek, nominal, note, mpin).collectLatest { result ->
                    when(result) {
                        is Resource.Loading -> {
                            _transferSesamaFormUiState.update { uiState ->
                                uiState.copy(isLoading = true, message = null, isSuccess = false, data = null)
                            }
                        }

                        is Resource.Success -> {
                            _transferSesamaFormUiState.update { uiState ->
                                uiState.copy(isLoading = false, message = result.message, isSuccess = true, data = result.data)
                            }
                        }

                        is Resource.Error -> {
                            _transferSesamaFormUiState.update { uiState ->
                                uiState.copy(isLoading = false, message = result.message, isSuccess = false, data = null)
                            }
                        }
                    }
                }
            } else {
                _transferSesamaFormUiState.update { uiState ->
                    uiState.copy(
                        isLoading = false,
                        message = "Tidak ada koneksi internet.",
                        isSuccess = false
                    )
                }
            }
        }
    }

    fun getDaftarTersimpanSesama() = viewModelScope.launch {
        val data = getDaftarTersimpanSesamaUseCase.invoke()
        _transferSesamaHomeUiState.update { uiState ->
            uiState.copy(data = data)
        }
    }

    fun cariDaftarTersimpanSesama(keyword: String) = viewModelScope.launch {
        cariDaftarTersimpanSesamaUseCase.invoke(keyword).collectLatest { data ->
            _transferSesamaHomeUiState.update { uiState ->
                uiState.copy(data = data)
            }
        }
    }

    fun simpanKeDaftarTersimpanSesama(namaPemilik: String, norek: String) {
        viewModelScope.launch {
            val data = DaftarTersimpan(0, namaPemilik, norek)
            tambahDaftarTersimpanSesamaUseCase.invoke(data)
        }
    }

    fun messageShown() {
        _transferSesamaFormUiState.update { currentUiState ->
            currentUiState.copy(message = null)
        }
    }

    fun transferSesamaBerhasil() {
        _transferSesamaFormUiState.update { currentUiState ->
            currentUiState.copy(isLoading = false, isSuccess = false, message = null, data = null)
        }
    }
}