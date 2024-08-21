package com.finsera.domain.usecase.transfer.ewallet

import com.finsera.domain.repository.IDaftarTersimpanRepository

class GetDaftarTersimpanEWalletUseCase (
    private val daftarTersimpanRepository: IDaftarTersimpanRepository
){
    suspend operator fun invoke() = daftarTersimpanRepository.getDaftarTersimpanEWallet()
}