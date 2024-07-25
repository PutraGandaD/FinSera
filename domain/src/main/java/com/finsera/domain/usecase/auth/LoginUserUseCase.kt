package com.finsera.domain.usecase.auth

import com.finsera.domain.repository.IAuthRepository

class LoginUserUseCase(private val repository: IAuthRepository) {
    operator suspend fun invoke(username: String, password: String) {
        repository.login(username, password)
    }
}