package com.finsera.domain.usecase.transfer.antar_bank

import com.finsera.domain.model.DaftarTersimpanAntar
import com.finsera.domain.repository.IDaftarTersimpanRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CariDaftarTersimpanAntarUseCase(
    private val daftarTersimpanRepository: IDaftarTersimpanRepository
) {
    suspend operator fun invoke(keyword: String) : Flow<List<DaftarTersimpanAntar>> = flow {
        emit(daftarTersimpanRepository.searchDaftarTersimpanAntar(keyword))
    }
}