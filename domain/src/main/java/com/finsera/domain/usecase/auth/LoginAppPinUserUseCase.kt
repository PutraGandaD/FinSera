package com.finsera.domain.usecase.auth

import com.finsera.common.utils.Resource
import com.finsera.domain.repository.IAuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LoginAppPinUserUseCase(private val repository: IAuthRepository) {
     operator fun invoke(pin: String) : Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val currentAppPin = repository.getAppLockPin()
            if(currentAppPin == pin) {
                val getRefreshToken = repository.getRefreshToken()
                repository.refreshAccessToken(getRefreshToken)
                emit(Resource.Success("Berhasil Login"))
            } else {
                emit(Resource.Error("PIN yang anda masukkan salah."))
            }
        } catch (t: Throwable) {
            emit(Resource.Error("Kesalahan pada server. Silahkan coba beberapa saat lagi."))
        }
    }
}