package com.finsera.domain.usecase.transfer.virtual_account

import com.finsera.domain.repository.IDaftarTersimpanRepository
import kotlinx.coroutines.flow.flow

class CariDaftarVaTersimpanUseCase (
    private val daftarVaTersimpanRepository: IDaftarTersimpanRepository
) {
    operator fun invoke(keyword: String) = flow {
        emit(daftarVaTersimpanRepository.searchDaftarTersimpanVa(keyword))
    }
}