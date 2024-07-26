package com.finsera.domain.repository

import com.finsera.domain.model.Saldo

interface ISaldoRepository {
    suspend fun getSaldo(token: String): Saldo
}