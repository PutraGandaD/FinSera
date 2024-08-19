package com.finsera.domain.usecase.transfer.sesama_bank

import com.finsera.domain.model.DaftarTersimpanSesama
import com.finsera.domain.repository.IDaftarTersimpanRepository

class TambahDaftarTersimpanSesamaUseCase(
    private val daftarTersimpanRepository: IDaftarTersimpanRepository
) {
    suspend operator fun invoke(daftarTersimpan : DaftarTersimpanSesama) {
        daftarTersimpanRepository.insertDaftarTersimpanSesama(daftarTersimpan)
    }
}