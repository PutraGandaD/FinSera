package com.finsera.domain.usecase.notifikasi

import com.finsera.domain.repository.INotifkasiRepository

class NotifikasiUseCase(
    private val repository: INotifkasiRepository
) {
    suspend operator fun invoke() = repository.getNotifikasi()
}