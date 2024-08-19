package com.finsera.domain.usecase.infosaldo

import com.finsera.common.utils.Resource
import com.finsera.domain.model.Saldo
import com.finsera.domain.repository.IAuthRepository
import com.finsera.domain.repository.ISaldoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class InfoSaldoUseCase (
    private val saldoRepository: ISaldoRepository,
    private val authRepository: IAuthRepository
) {
    suspend operator fun invoke(): Flow<Resource<Saldo>> = flow {
        emit(Resource.Loading())
        try {
            val saldo = saldoRepository.getSaldo()
            val accountNum = saldo.accountNumber
            val accountUserName = saldo.username
            authRepository.saveUserInfo(accountUserName, accountNum)
            emit(Resource.Success(saldo))
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }
}