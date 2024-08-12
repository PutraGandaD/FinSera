package com.finsera.domain.usecase.transfer.sesama_bank

import com.finsera.domain.model.DaftarTersimpan
import com.finsera.domain.repository.IDaftarTersimpanRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CariDaftarTersimpanSesamaUseCase(
    private val daftarTersimpanRepository: IDaftarTersimpanRepository
) {
    operator fun invoke(keyword: String) : Flow<List<DaftarTersimpan>> = flow {
        emit(daftarTersimpanRepository.searchDaftarTersimpanSesama(keyword))
    }
}