package com.finsera.domain.usecase.auth

import com.finsera.common.utils.Resource
import com.finsera.domain.repository.IAuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ChangeAppPinUseCase(
    private val authRepository: IAuthRepository
) {
    operator fun invoke(oldPin: String, newPin: String, confirmPin: String) : Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val getOldPin = authRepository.getAppLockPin()

            if (getOldPin != oldPin) {
                emit(Resource.Error("PIN Lama anda salah!"))
                return@flow
            } else if(newPin != confirmPin) {
                emit(Resource.Error("PIN baru dan Konfirmasi PIN Baru tidak sama!"))
                return@flow
            } else if(newPin.length != 6) {
                emit(Resource.Error("PIN baru harus 6 digit"))
                return@flow
            }

            authRepository.setAppLockPin(newPin)

            emit(Resource.Success("PIN aplikasi berhasil diubah!"))
        } catch (e: Exception) {
            emit(Resource.Error("Ada kesalahan terjadi pada aplikasi, Mohon Coba Lagi."))
        }
    }
}