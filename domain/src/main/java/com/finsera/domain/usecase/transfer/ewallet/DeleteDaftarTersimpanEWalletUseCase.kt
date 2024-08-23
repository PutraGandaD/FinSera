package com.finsera.domain.usecase.transfer.ewallet

import com.finsera.domain.model.DaftarTersimpanAntar
import com.finsera.domain.model.DaftarTersimpanEWallet
import com.finsera.domain.repository.IDaftarTersimpanRepository

class DeleteDaftarTersimpanEWalletUseCase(
    private val repository: IDaftarTersimpanRepository
) {
    suspend operator fun invoke(daftarTersimpanEWallet : DaftarTersimpanEWallet) {
        repository.deleteDaftarTersimpanEWallet(daftarTersimpanEWallet)
    }
}