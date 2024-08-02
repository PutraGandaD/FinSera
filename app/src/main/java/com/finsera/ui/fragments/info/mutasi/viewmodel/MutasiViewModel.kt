package com.finsera.ui.fragments.info.mutasi.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finsera.common.utils.Resource
import com.finsera.domain.model.Mutasi
import com.finsera.domain.usecase.mutasi.MutasiUseCase
import kotlinx.coroutines.launch

class MutasiViewModel (private val mutasiUseCase: MutasiUseCase): ViewModel(){

    private val _mutasi = MutableLiveData<Resource<List<Mutasi>>>()
    val mutasi: LiveData<Resource<List<Mutasi>>> get() = _mutasi

    fun getMutasi(startDate: String?, endDate: String?, page: Int = 1, size: Int = 10) {
        viewModelScope.launch {
            mutasiUseCase.invoke(startDate, endDate, page, size).collect {
                _mutasi.postValue(it)
            }
        }
    }
}