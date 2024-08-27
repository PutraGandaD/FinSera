package com.finsera.domain.usecase.auth

import com.finsera.common.utils.Resource
import com.finsera.domain.repository.IAuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LoginAppFingerprintUseCase(private val repository: IAuthRepository) {
    operator fun invoke() : Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val getRefreshToken = repository.getRefreshToken()
            repository.refreshAccessToken(getRefreshToken)
            emit(Resource.Success("Berhasil Login"))
        } catch (t: Throwable) {
            emit(Resource.Error("Kesalahan pada server. Silahkan coba beberapa saat lagi."))
        }
    }
}