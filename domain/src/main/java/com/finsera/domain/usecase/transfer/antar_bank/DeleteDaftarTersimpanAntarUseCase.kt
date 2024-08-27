package com.finsera.domain.usecase.transfer.antar_bank

import com.finsera.domain.model.DaftarTersimpanAntar
import com.finsera.domain.repository.IDaftarTersimpanRepository

class DeleteDaftarTersimpanAntarUseCase(
    private val repository : IDaftarTersimpanRepository
) {
    suspend operator fun invoke(daftarTersimpanAntar : DaftarTersimpanAntar) {
        repository.deleteDaftarTersimpanAntar(daftarTersimpanAntar)
    }
}