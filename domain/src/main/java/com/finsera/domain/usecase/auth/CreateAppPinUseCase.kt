package com.finsera.domain.usecase.auth

import com.finsera.domain.repository.IAuthRepository

class CreateAppPinUseCase(private val repository: IAuthRepository) {
    operator fun invoke(newPin: String, confirmNewPin: String) : Boolean  {
        val result = if(newPin == confirmNewPin) {
            repository.setAppLockPin(newPin)
            true
        } else {
            false
        }

        return result
    }
}