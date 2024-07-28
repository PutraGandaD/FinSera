package com.finsera.ui.fragments.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finsera.common.utils.Resource
import com.finsera.domain.model.Saldo
import com.finsera.domain.usecase.infosaldo.InfoSaldoUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeViewModel(private val infoSaldoUseCase: InfoSaldoUseCase) : ViewModel() {
    private val _saldo: MutableLiveData<Saldo> = MutableLiveData()
    val saldo: LiveData<Saldo>
        get() = _saldo

    private val _loading: MutableLiveData<Boolean> = MutableLiveData()
    val loading: LiveData<Boolean> = _loading


    //message
    private val _message: MutableLiveData<String> = MutableLiveData()
    val message: LiveData<String> = _message

    private val _isSaldoVisible = MutableLiveData<Boolean>().apply { value = false }
    val isSaldoVisible: LiveData<Boolean> get() = _isSaldoVisible

    init {
        getSaldo()
    }

    private fun getSaldo(){
        viewModelScope.launch {
            infoSaldoUseCase.invoke().collect {
                when(it){
                    is Resource.Loading -> {
                        _loading.postValue(true)
                    }
                    is Resource.Success -> {
                        _loading.postValue(false)
                        _saldo.postValue(it.data!!)
                    }
                    is Resource.Error -> {
                        _loading.postValue(false)
                        _message.postValue(it.message!!)
                    }
                }
            }
        }
    }

    fun toggleSaldoVisibility() {
        _isSaldoVisible.value = _isSaldoVisible.value != true
    }
}