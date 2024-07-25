package com.finsera.ui.fragments.auth.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finsera.domain.usecase.auth.LoginUserUseCase
import kotlinx.coroutines.launch

class AuthViewModel(
    private val loginUserUseCase: LoginUserUseCase
) : ViewModel() {

    fun userLogin(username: String, password: String) {
        viewModelScope.launch {
            loginUserUseCase.invoke(username, password)
        }
    }
}