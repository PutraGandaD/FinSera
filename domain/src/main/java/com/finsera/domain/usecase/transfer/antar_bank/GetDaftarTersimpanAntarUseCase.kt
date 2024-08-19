package com.finsera.domain.usecase.transfer.antar_bank

import com.finsera.domain.model.DaftarTersimpanAntar
import com.finsera.domain.repository.IDaftarTersimpanRepository

class GetDaftarTersimpanAntarUseCase(
    private val daftarTersimpanRepository: IDaftarTersimpanRepository
) {
    suspend operator fun invoke() : List<DaftarTersimpanAntar> {
        return daftarTersimpanRepository.getDaftarTersimpanAntar()
    }
}