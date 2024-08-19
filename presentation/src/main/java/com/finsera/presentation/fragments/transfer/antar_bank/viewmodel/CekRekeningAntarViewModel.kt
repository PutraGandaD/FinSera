package com.finsera.presentation.fragments.transfer.antar_bank.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finsera.common.utils.Resource
import com.finsera.common.utils.network.ConnectivityManager
import com.finsera.domain.usecase.transfer.antar_bank.CekRekeningAntarUseCase
import com.finsera.domain.usecase.transfer.antar_bank.GetListBankUseCase
import com.finsera.presentation.fragments.transfer.antar_bank.uistate.CekRekeningAntarUiState
import com.finsera.presentation.fragments.transfer.antar_bank.uistate.ListBankUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CekRekeningAntarViewModel(
    private val connectivityManager: ConnectivityManager,
    private val getListBankUseCase: GetListBankUseCase,
    private val cekRekeningAntarUseCase: CekRekeningAntarUseCase
) : ViewModel() {
    private val _listBankModalSheetUiState = MutableStateFlow(ListBankUiState())
    val listBankModalSheetUiState = _listBankModalSheetUiState.asStateFlow()

    private val _cekRekeningAntarUiState = MutableStateFlow(CekRekeningAntarUiState())
    val cekRekeningAntarUiState = _cekRekeningAntarUiState.asStateFlow()

    private var _namaBankSelected = MutableLiveData<String?>()
    val namaBankSelected : LiveData<String?>
        get() = _namaBankSelected

    private var _idBankSelected : Int? = null
    val idBankSelected : Int?
        get() = _idBankSelected

    fun getListBank() = viewModelScope.launch {
        if(connectivityManager.hasInternetConnection()) {
            getListBankUseCase.invoke().collectLatest { result ->
                when(result) {
                    is Resource.Loading -> {
                        _listBankModalSheetUiState.update { uiState ->
                            uiState.copy(data = emptyList(), message = null, isLoading = true, isSuccess = false)
                        }
                    }

                    is Resource.Success -> {
                        _listBankModalSheetUiState.update { uiState ->
                            uiState.copy(data = result.data, message = null, isLoading = false, isSuccess = true)
                        }
                    }

                    is Resource.Error -> {
                        _listBankModalSheetUiState.update { uiState ->
                            uiState.copy(data = emptyList(), message = result.message, isLoading = false, isSuccess = false)
                        }
                    }
                }
            }
        } else {
            _listBankModalSheetUiState.update { uiState ->
                uiState.copy(data = emptyList(), message = "Tidak ada koneksi internet.", isLoading = false, isSuccess = false)
            }
        }
    }

    fun cekRekeningAntar(idBank: Int, noRek: String) = viewModelScope.launch {
        if(connectivityManager.hasInternetConnection()) {
            cekRekeningAntarUseCase.invoke(idBank, noRek).collectLatest { result ->
                when(result) {
                    is Resource.Loading -> {
                        _cekRekeningAntarUiState.update { uiState ->
                            uiState.copy(isLoading = true, data = null, message = null, isValid = false)
                        }
                    }
                    is Resource.Success -> {
                        _cekRekeningAntarUiState.update { uiState ->
                            uiState.copy(isLoading = false, data = result.data, message = null, isValid = true)
                        }
                    }
                    is Resource.Error -> {
                        _cekRekeningAntarUiState.update { uiState ->
                            uiState.copy(isLoading = false, data = null, message = result.message, isValid = false)
                        }
                    }
                }
            }
        } else {
            _cekRekeningAntarUiState.update { uiState ->
                uiState.copy(isLoading = false, data = null, message = "Tidak ada koneksi internet", isValid = false)
            }
        }
    }

    fun bottomSheetMessageShown() {
        _listBankModalSheetUiState.update { uiState ->
            uiState.copy(message = null)
        }
    }

    fun cekRekeningMessageShown() {
        _cekRekeningAntarUiState.update { uiState ->
            uiState.copy(message = null)
        }
    }

    fun setBankDetails(idBank: Int, namaBank: String) {
        _idBankSelected = idBank
        _namaBankSelected.value = namaBank
    }

    fun resetBankDetails() {
        _namaBankSelected.value = null
        _idBankSelected = null
    }

    fun redirectedToKonfirmasiForm() {
        _cekRekeningAntarUiState.update { currentUiState ->
            currentUiState.copy(isLoading = false, isValid = false)
        }
    }
}