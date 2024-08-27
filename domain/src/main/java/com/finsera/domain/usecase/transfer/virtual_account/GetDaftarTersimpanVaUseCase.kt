package com.finsera.domain.usecase.transfer.virtual_account

import com.finsera.domain.repository.IDaftarTersimpanRepository

class GetDaftarTersimpanVaUseCase(
    private val daftarTersimpanRepository: IDaftarTersimpanRepository
) {
    suspend operator fun invoke() = daftarTersimpanRepository.getDaftarTersimpanVa()
}