package com.finsera.domain.usecase.transfer.sesama_bank

import com.finsera.domain.model.DaftarTersimpanSesama
import com.finsera.domain.repository.IDaftarTersimpanRepository

class DeleteDaftarTersimpanSesamaUseCase(
    private val repository: IDaftarTersimpanRepository
) {
    suspend operator fun invoke(daftarTersimpanSesama: DaftarTersimpanSesama) {
        repository.deleteDaftarTersimpanSesama(daftarTersimpanSesama)
    }
}