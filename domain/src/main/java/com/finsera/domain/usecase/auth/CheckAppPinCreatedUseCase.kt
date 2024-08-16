package com.finsera.domain.usecase.auth

import com.finsera.domain.repository.IAuthRepository

class CheckAppPinCreatedUseCase(private val repository: IAuthRepository) {
    operator fun invoke() : Boolean {
        val getCurrentAppPin = repository.getAppLockPin()
        val result = getCurrentAppPin != ""

        return result
    }
}