package com.finsera.domain.usecase.auth

import com.finsera.domain.repository.IAuthRepository

class LogoutUserUseCase(private val repository: IAuthRepository) {
    operator fun invoke() {
        repository.clearSharedPreferences() // clear
        repository.setLoginStatus(false) // set login status to false
    }
}