package com.finsera.domain.usecase.transfer.ewallet

import com.finsera.domain.repository.IDaftarTersimpanRepository
import kotlinx.coroutines.flow.flow

class CariDaftarEWalletTersimpanUseCase(
    private val daftarEWalletTersimpanRepository: IDaftarTersimpanRepository
) {
    operator fun invoke(keyword: String) = flow{
        emit(daftarEWalletTersimpanRepository.searchDaftarTersimpanEWallet(keyword))
    }
}