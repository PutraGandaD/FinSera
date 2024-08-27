package com.finsera.presentation.fragments.favorit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finsera.domain.model.DaftarTersimpanAntar
import com.finsera.domain.model.DaftarTersimpanEWallet
import com.finsera.domain.model.DaftarTersimpanSesama
import com.finsera.domain.model.DaftarTersimpanVa
import com.finsera.domain.usecase.transfer.antar_bank.DeleteDaftarTersimpanAntarUseCase
import com.finsera.domain.usecase.transfer.antar_bank.GetDaftarTersimpanAntarUseCase
import com.finsera.domain.usecase.transfer.ewallet.DeleteDaftarTersimpanEWalletUseCase
import com.finsera.domain.usecase.transfer.ewallet.GetDaftarTersimpanEWalletUseCase
import com.finsera.domain.usecase.transfer.sesama_bank.DeleteDaftarTersimpanSesamaUseCase
import com.finsera.domain.usecase.transfer.sesama_bank.GetDaftarTersimpanSesamaUseCase
import com.finsera.domain.usecase.transfer.virtual_account.DeleteDaftarTersimpanVaUseCase
import com.finsera.domain.usecase.transfer.virtual_account.GetDaftarTersimpanVaUseCase
import com.finsera.presentation.fragments.favorit.uistate.FavoritUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FavoritViewModel(
    private val getDaftarTersimpanSesamaUseCase: GetDaftarTersimpanSesamaUseCase,
    private val getDaftarTersimpanAntarUseCase: GetDaftarTersimpanAntarUseCase,
    private val getDaftarTersimpanEWalletUseCase: GetDaftarTersimpanEWalletUseCase,
    private val getDaftarTersimpanVaUseCase: GetDaftarTersimpanVaUseCase,
    private val deleteDaftarTersimpanSesamaUseCase: DeleteDaftarTersimpanSesamaUseCase,
    private val deleteDaftarTersimpanAntarUseCase: DeleteDaftarTersimpanAntarUseCase,
    private val deleteDaftarTersimpanEWalletUseCase: DeleteDaftarTersimpanEWalletUseCase,
    private val deleteDaftarTersimpanVaUseCase: DeleteDaftarTersimpanVaUseCase
) : ViewModel() {
    private val _favoritUiState = MutableStateFlow(FavoritUiState())
    val favoritUiState = _favoritUiState.asStateFlow()

    init {
        getData()
    }

    private fun getData() {
        getDaftarTersimpanSesama()
        getDaftarTersimpanAntar()
        getDaftarTersimpanEWallet()
        getDaftarTersimpanVa()
    }

    private fun getDaftarTersimpanSesama() = viewModelScope.launch {
        val data = getDaftarTersimpanSesamaUseCase.invoke()
        _favoritUiState.update { uiState ->
            uiState.copy(dataFavoritSesama = data)
        }
    }

    private fun getDaftarTersimpanAntar() = viewModelScope.launch {
        val data = getDaftarTersimpanAntarUseCase.invoke()
        _favoritUiState.update { uiState ->
            uiState.copy(dataFavoritAntar = data)
        }
    }

    private fun getDaftarTersimpanEWallet() = viewModelScope.launch {
        val data = getDaftarTersimpanEWalletUseCase.invoke()
        _favoritUiState.update { uiState ->
            uiState.copy(dataFavoritEWallet = data)
        }
    }

    private fun getDaftarTersimpanVa() = viewModelScope.launch {
        val data = getDaftarTersimpanVaUseCase.invoke()
        _favoritUiState.update { uiState ->
            uiState.copy(dataFavoritVirtualAccount = data)
        }
    }

    fun deleteDaftarTersimpanSesama(data: DaftarTersimpanSesama) = viewModelScope.launch {
        deleteDaftarTersimpanSesamaUseCase.invoke(data)
        getData()
    }

    fun deleteDaftarTersimpanAntar(data: DaftarTersimpanAntar) = viewModelScope.launch {
        deleteDaftarTersimpanAntarUseCase.invoke(data)
        getData()
    }

    fun deleteDaftarTersimpanEWallet(data: DaftarTersimpanEWallet) = viewModelScope.launch {
        deleteDaftarTersimpanEWalletUseCase.invoke(data)
        getData()
    }

    fun deleteDaftarTersimpanVirtualAccount(data: DaftarTersimpanVa) = viewModelScope.launch {
        deleteDaftarTersimpanVaUseCase.invoke(data)
        getData()
    }
}