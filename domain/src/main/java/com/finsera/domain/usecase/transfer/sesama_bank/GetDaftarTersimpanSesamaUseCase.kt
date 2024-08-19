package com.finsera.domain.usecase.transfer.sesama_bank

import com.finsera.domain.model.DaftarTersimpanSesama
import com.finsera.domain.repository.IDaftarTersimpanRepository

class GetDaftarTersimpanSesamaUseCase(
    private val daftarTersimpanRepository: IDaftarTersimpanRepository
) {
    suspend operator fun invoke() : List<DaftarTersimpanSesama> {
        return daftarTersimpanRepository.getDaftarTersimpanSesama()
    }
}