package com.finsera.presentation.fragments.transfer.antar_bank.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finsera.domain.usecase.transfer.antar_bank.CariDaftarTersimpanAntarUseCase
import com.finsera.domain.usecase.transfer.antar_bank.GetDaftarTersimpanAntarUseCase
import com.finsera.presentation.fragments.transfer.antar_bank.uistate.TransferAntarHomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TransferAntarBankHomeViewModel(
    private val getDaftarTersimpanAntarUseCase: GetDaftarTersimpanAntarUseCase,
    private val cariDaftarTersimpanAntarUseCase: CariDaftarTersimpanAntarUseCase
) : ViewModel() {
    private val _transferAntarHomeUiState = MutableStateFlow(TransferAntarHomeUiState())
    val transferAntarHomeUiState = _transferAntarHomeUiState.asStateFlow()

    init {
        getDaftarTersimpanAntar()
    }

    fun getDaftarTersimpanAntar() = viewModelScope.launch {
        val data = getDaftarTersimpanAntarUseCase.invoke()
        _transferAntarHomeUiState.update { uiState ->
            uiState.copy(data = data)
        }
    }

    fun cariDaftarTersimpanAntar(keyword: String) = viewModelScope.launch {
        cariDaftarTersimpanAntarUseCase.invoke(keyword).collectLatest { data ->
            _transferAntarHomeUiState.update { uiState ->
                uiState.copy(data = data)
            }
        }
    }
}