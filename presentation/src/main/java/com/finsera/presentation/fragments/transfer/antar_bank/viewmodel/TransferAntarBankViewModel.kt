package com.finsera.presentation.fragments.transfer.antar_bank.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finsera.common.utils.Resource
import com.finsera.common.utils.network.ConnectivityManager
import com.finsera.domain.model.DaftarTersimpanAntar
import com.finsera.domain.model.DaftarTersimpanSesama
import com.finsera.domain.usecase.auth.GetUserInfoUseCase
import com.finsera.domain.usecase.transfer.antar_bank.TambahDaftarTersimpanAntarUseCase
import com.finsera.domain.usecase.transfer.antar_bank.TransferAntarBankUseCase
import com.finsera.presentation.fragments.transfer.antar_bank.uistate.TransferAntarFormKonfirmasiUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TransferAntarBankViewModel(
    private val connectivityManager: ConnectivityManager,
    private val transferAntarBankUseCase: TransferAntarBankUseCase,
    private val tambahDaftarTersimpanAntarUseCase: TambahDaftarTersimpanAntarUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase
) : ViewModel() {
    private val _transferAntarFormKonfirmasiUiState = MutableStateFlow(
        TransferAntarFormKonfirmasiUiState()
    )
    val transferAntarFormKonfirmasiUiState = _transferAntarFormKonfirmasiUiState.asStateFlow()

    private var _userInfo : Pair<String, String>? = null
    val userInfo : Pair<String, String>?
        get() = _userInfo

    init {
        getUserInfo()
    }

    fun transferAntar(idBank: Int, noRek: String, nominal: Double, note: String, mpin: String) {
        viewModelScope.launch {
            if(connectivityManager.hasInternetConnection()) {
                transferAntarBankUseCase.invoke(idBank, noRek, nominal, note, mpin).collectLatest { result ->
                    when(result) {
                        is Resource.Loading -> {
                            _transferAntarFormKonfirmasiUiState.update { uiState ->
                                uiState.copy(isLoading = true, message = null, isSuccess = false, data = null)
                            }
                        }

                        is Resource.Error -> {
                            _transferAntarFormKonfirmasiUiState.update { uiState ->
                                uiState.copy(isLoading = false, message = result.message, isSuccess = false, data = null)
                            }
                        }

                        is Resource.Success -> {
                            _transferAntarFormKonfirmasiUiState.update { uiState ->
                                uiState.copy(isLoading = false, message = null, isSuccess = true, data = result.data)
                            }
                        }
                    }
                }
            } else {
                _transferAntarFormKonfirmasiUiState.update { uiState ->
                    uiState.copy(isLoading = false, message = "Tidak ada koneksi internet", isSuccess = false, data = null)
                }
            }
        }
    }

    fun messageFormKonfirmasiShown() {
        _transferAntarFormKonfirmasiUiState.update { currentUiState ->
            currentUiState.copy(message = null)
        }
    }


    fun transferAntarBerhasil() {
        _transferAntarFormKonfirmasiUiState.update { currentUiState ->
            currentUiState.copy(isLoading = false, isSuccess = false, message = null, data = null)
        }
    }

    fun simpanKeDaftarTersimpanAntar(idBank: Int, namaBank: String, namaPemilik: String, norek: String) {
        viewModelScope.launch {
            val data = DaftarTersimpanAntar(0, idBank, namaBank, namaPemilik, norek)
            tambahDaftarTersimpanAntarUseCase.invoke(data)
        }
    }

    private fun getUserInfo() {
        _userInfo = getUserInfoUseCase.invoke()
    }
}