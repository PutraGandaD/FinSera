package com.finsera.presentation.fragments.transfer.sesama_bank.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finsera.domain.usecase.transfer.sesama_bank.CariDaftarTersimpanSesamaUseCase
import com.finsera.domain.usecase.transfer.sesama_bank.GetDaftarTersimpanSesamaUseCase
import com.finsera.presentation.fragments.transfer.sesama_bank.uistate.TransferSesamaHomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TransferSesamaBankHomeViewModel(
    private val getDaftarTersimpanSesamaUseCase: GetDaftarTersimpanSesamaUseCase,
    private val cariDaftarTersimpanSesamaUseCase: CariDaftarTersimpanSesamaUseCase
) : ViewModel() {
    private val _transferSesamaHomeUiState = MutableStateFlow(TransferSesamaHomeUiState())
    val transferSesamaHomeUiState = _transferSesamaHomeUiState.asStateFlow()

    init {
        getDaftarTersimpanSesama()
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
}