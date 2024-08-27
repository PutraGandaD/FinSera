package com.finsera.domain.usecase.transfer.virtual_account

import com.finsera.domain.model.DaftarTersimpanVa
import com.finsera.domain.repository.IDaftarTersimpanRepository

class DeleteDaftarTersimpanVaUseCase(
    private val repository: IDaftarTersimpanRepository
) {
    suspend operator fun invoke(daftarTersimpanVa: DaftarTersimpanVa) {
        repository.deleteDaftarTersimpanVa(daftarTersimpanVa)
    }
}