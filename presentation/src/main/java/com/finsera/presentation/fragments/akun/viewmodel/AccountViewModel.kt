package com.finsera.presentation.fragments.akun.viewmodel

import androidx.lifecycle.ViewModel
import com.finsera.domain.usecase.auth.GetUserInfoUseCase
import com.finsera.domain.usecase.auth.LogoutUserUseCase

class AccountViewModel(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val logoutUserUseCase: LogoutUserUseCase
) : ViewModel() {
    private var _userInfo : Pair<String, String>? = null
    val userInfo : Pair<String, String>?
        get() = _userInfo

    init {
        getUserInfo()
    }

    private fun getUserInfo() {
        _userInfo = getUserInfoUseCase.invoke()
    }

    fun logout() {
        logoutUserUseCase.invoke()
    }
}