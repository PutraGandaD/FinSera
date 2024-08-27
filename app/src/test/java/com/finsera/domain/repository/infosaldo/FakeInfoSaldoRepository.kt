package com.finsera.domain.repository.infosaldo

import com.finsera.data.utils.DataMapper
import com.finsera.domain.model.Saldo
import com.finsera.domain.repository.FakeRemoteDataSource
import com.finsera.domain.repository.ISaldoRepository

class FakeInfoSaldoRepository(
    private val fakeRemoteDataSource: FakeRemoteDataSource
):ISaldoRepository {
    override suspend fun getSaldo(): Saldo {
        val accessToken = "token"
        val response = fakeRemoteDataSource.getSaldo(accessToken)
        return DataMapper.infoSaldoResponseToDomain(response)
    }

}