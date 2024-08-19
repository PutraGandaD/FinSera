package com.finsera.domain.usecase.transfer.antar_bank

import com.finsera.domain.model.DaftarTersimpanAntar
import com.finsera.domain.repository.IDaftarTersimpanRepository

class TambahDaftarTersimpanAntarUseCase(
    private val daftarTersimpanRepository: IDaftarTersimpanRepository
) {
    suspend operator fun invoke(daftarTersimpan: DaftarTersimpanAntar) {
        daftarTersimpanRepository.insertDaftarTersimpanAntar(daftarTersimpan)
    }
}