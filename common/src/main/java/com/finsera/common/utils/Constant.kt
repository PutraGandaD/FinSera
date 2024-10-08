package com.finsera.common.utils

class Constant {
    companion object {
        const val BASE_URL = "https://finsera-api.site/api/v1/"
        const val USER_ACCESS_TOKEN_KEY = "USER_ACCESS_TOKEN" // need to be cleanable
        const val USER_REFRESH_TOKEN_KEY = "USER_REFRESH_TOKEN" // need to be cleanable
        const val USER_LOGGED_IN_STATUS = "USER_LOGGED_IN_STATUS" // need to be cleanable
        const val USER_APPLICATION_PIN = "APP_PIN" // need to be cleanable
        const val FINSERA_SHARED_PREFERENCES = "Finsera_App_Preferences"

        const val USERNAME_NASABAH = "USERNAME_NASABAH"
        const val NOMOR_REKENING_NASABAH = "NO_REK" // need to be cleanable
        const val NAMA_NASABAH = "NAMA_NASABAH" // need to be cleanable

        const val DATA_REKENING_SESAMA_BUNDLE = "DATA_REKENING_SESAMA_BUNDLE"
        const val DATA_REKENING_ANTAR_BUNDLE = "DATA_REKENING_ANTAR_BUNDLE"
        const val NOMINAL_TRANSFER_EXTRA = "NOMINAL_TF_BUNDLE"
        const val CATATAN_TRANSFER_EXTRA = "CATATAN_TF_EXTRA"
        const val DAFTAR_TERSIMPAN_CHECKED_EXTRA = "DAFTAR_TERSIMPAN_CHECKED"
        const val DAFTAR_TERSIMPAN_SELECTED_MODE = "DAFTAR_TERSIMPAN_SELECTED_MODE"

        const val NAMA_MERCHANT_QRIS = "NAMA_MERCHANT_QRIS"
        const val NAMA_KOTA_MERCHANT_QRIS = "NAMA_KOTA_MERCHANT_QRIS"
        const val NOMOR_TRX_MERCHANT_QRIS = "NO_TRX_MERCHANT_QRIS"

        const val DATA_VA_BUNDLE = "DATA_VA_BUNDLE"
        const val DATA_NO_VA_STRING="DATA_NO_VA_STRING"
        const val DATA_TF_VA_BUNDLE = "DATA_TF_VA_BUNDLE"

        const val DATA_ID_EWALLET= "DATA_ID_EWALLET"
        const val DATA_CEK_EWALLET= "DATA_CEK_EWALLET"
        const val DATA_TF_EWALLET_BUNDLE = "DATA_TF_EWALLET_BUNDLE"

        const val TRANSFER_SESAMA_BERHASIL_BUNDLE = "TRANSFER_SESAMA_BERHASIL_BUNDLE"
        const val TRANSFER_ANTAR_BERHASIL_BUNDLE = "TRANSFER_ANTAR_BERHASIL_BUNDLE"
        const val TRANSFER_QRIS_MERCHANT_BERHASIL_BUNDLE = "QRIS_TF_SUCCESS_BUNDLE"
    }
}

