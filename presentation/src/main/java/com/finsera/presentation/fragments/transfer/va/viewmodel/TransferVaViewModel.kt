package com.finsera.presentation.fragments.transfer.va.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finsera.common.utils.Resource
import com.finsera.common.utils.network.ConnectivityManager
import com.finsera.domain.model.DaftarTersimpan
import com.finsera.domain.usecase.transfer.sesama_bank.CariDaftarTersimpanSesamaUseCase
import com.finsera.domain.usecase.transfer.sesama_bank.GetDaftarTersimpanSesamaUseCase
import com.finsera.domain.usecase.transfer.sesama_bank.TambahDaftarTersimpanSesamaUseCase
import com.finsera.domain.usecase.transfer.virtual_account.CariDaftarVaTersimpanUseCase
import com.finsera.domain.usecase.transfer.virtual_account.GetDaftarTersimpanVaUseCase
import com.finsera.domain.usecase.transfer.virtual_account.TambahDaftarTersimpanVaUseCase
import com.finsera.domain.usecase.transfer.virtual_account.TransferVaUseCase
import com.finsera.presentation.fragments.transfer.sesama_bank.uistate.TransferSesamaHomeUiState
import com.finsera.presentation.fragments.transfer.va.uistate.TransferVaFormConfirmUiState
import com.finsera.presentation.fragments.transfer.va.uistate.TransferVaHomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TransferVaViewModel(
    private val connectivityManager: ConnectivityManager,
    private val transferVaUseCase: TransferVaUseCase,
    private val getDaftarTersimpanVaUseCase: GetDaftarTersimpanVaUseCase,
    private val tambahDaftarTersimpanVaUseCase: TambahDaftarTersimpanVaUseCase,
    private val cariDaftarTersimpanVaUseCase: CariDaftarVaTersimpanUseCase
) : ViewModel() {

    private val _transferVaHomeUiState = MutableStateFlow(TransferVaHomeUiState())
    val transferVaHomeUiState = _transferVaHomeUiState.asStateFlow()


    private val _transferVaUiState = MutableStateFlow(TransferVaFormConfirmUiState())
    val transferVaUiState = _transferVaUiState.asStateFlow()

    init {
        getDaftarTersimpanVa()
    }

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

    fun getDaftarTersimpanVa() = viewModelScope.launch {
        val data = getDaftarTersimpanVaUseCase.invoke()
        _transferVaHomeUiState.update { uiState ->
            uiState.copy(data = data)
        }
    }

    fun cariDaftarTersimpanVa(keyword: String) = viewModelScope.launch {
        cariDaftarTersimpanVaUseCase.invoke(keyword).collectLatest { data ->
            _transferVaHomeUiState.update { uiState ->
                uiState.copy(data = data)
            }
        }
    }

    fun simpanKeDaftarTersimpanVa(namaPemilik: String, noRek: String, nominal : Int) {
        viewModelScope.launch {
            val data = DaftarTersimpan(0, namaPemilik, noRek,nominal)
            tambahDaftarTersimpanVaUseCase.invoke(data)
        }
    }
}