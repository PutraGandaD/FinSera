package com.finsera.domain.usecase.transfer.ewallet

import com.finsera.domain.model.DaftarTersimpanEWallet
import com.finsera.domain.repository.IDaftarTersimpanRepository

class TambahDaftarTersimpanEWalletUseCase (
    private val daftarTersimpanRepository: IDaftarTersimpanRepository
){
    suspend operator fun invoke(daftarTersimpan: DaftarTersimpanEWallet) {
        daftarTersimpanRepository.insertDaftarTersimpanEWallet(daftarTersimpan)
    }
}