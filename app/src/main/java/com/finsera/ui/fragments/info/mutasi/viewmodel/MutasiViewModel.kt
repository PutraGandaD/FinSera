package com.finsera.ui.fragments.info.mutasi.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finsera.common.utils.Resource
import com.finsera.domain.model.Mutasi
import com.finsera.domain.usecase.mutasi.MutasiUseCase
import com.finsera.ui.fragments.info.mutasi.uistate.MutasiUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MutasiViewModel(private val mutasiUseCase: MutasiUseCase) : ViewModel() {

    private val _mutasi = MutableLiveData<Resource<List<Mutasi>>>()
    val mutasi: LiveData<Resource<List<Mutasi>>> get() = _mutasi

    private val _mutasiUiState = MutableStateFlow(MutasiUiState())
    val mutasiUiState = _mutasiUiState

    fun getMutasi(startDate: String?, endDate: String?, page: Int = 1, size: Int = 10) {
        viewModelScope.launch {
            mutasiUseCase.invoke(startDate, endDate, page, size).collect {
                val currentState = _mutasiUiState.value
                when (it) {
                    is Resource.Loading -> {
                        _mutasiUiState.value = currentState.copy(isLoading = true, message = null)
                    }

                    is Resource.Success -> {
                        _mutasiUiState.value = currentState.copy(
                            isLoading = false,
                            mutasi = it.data ?: emptyList(),
                            message = null
                        )
                    }

                    is Resource.Error -> {
                        _mutasiUiState.value =
                            currentState.copy(isLoading = false, message = it.message)
                    }
                }
            }
        }
    }
}