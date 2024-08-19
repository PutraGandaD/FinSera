package com.finsera.domain.usecase.auth

import com.finsera.domain.repository.IAuthRepository

class GetUserInfoUseCase(
    private val authRepository: IAuthRepository
) {
    operator fun invoke() : Pair<String, String> {
        return authRepository.getUserInfo()
    }
}