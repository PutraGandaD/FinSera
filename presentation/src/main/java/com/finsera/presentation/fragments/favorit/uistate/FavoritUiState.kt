package com.finsera.presentation.fragments.favorit.uistate

import com.finsera.domain.model.DaftarTersimpanAntar
import com.finsera.domain.model.DaftarTersimpanEWallet
import com.finsera.domain.model.DaftarTersimpanSesama
import com.finsera.domain.model.DaftarTersimpanVa

data class FavoritUiState(
    val dataFavoritSesama: List<DaftarTersimpanSesama>? = null,
    val dataFavoritAntar: List<DaftarTersimpanAntar>? = null,
    val dataFavoritEWallet: List<DaftarTersimpanEWallet>? = null,
    val dataFavoritVirtualAccount : List<DaftarTersimpanVa>? = null
)