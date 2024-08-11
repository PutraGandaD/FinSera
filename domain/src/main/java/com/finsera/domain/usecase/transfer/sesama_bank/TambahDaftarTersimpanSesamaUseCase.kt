package com.finsera.domain.usecase.transfer.sesama_bank

import com.finsera.domain.model.DaftarTersimpan
import com.finsera.domain.repository.IDaftarTersimpanRepository

class TambahDaftarTersimpanSesamaUseCase(
    private val daftarTersimpanRepository: IDaftarTersimpanRepository
) {
    suspend operator fun invoke(daftarTersimpan : DaftarTersimpan) {
        daftarTersimpanRepository.insertDaftarTersimpanSesama(daftarTersimpan)
    }
}