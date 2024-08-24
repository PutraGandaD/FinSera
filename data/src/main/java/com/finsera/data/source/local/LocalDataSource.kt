package com.finsera.data.source.local

import com.finsera.common.utils.Constant
import com.finsera.common.utils.Constant.Companion.USER_ACCESS_TOKEN_KEY
import com.finsera.common.utils.Constant.Companion.USER_APPLICATION_PIN
import com.finsera.common.utils.Constant.Companion.USER_LOGGED_IN_STATUS
import com.finsera.common.utils.Constant.Companion.USER_REFRESH_TOKEN_KEY
import com.finsera.common.utils.sharedpref.SharedPreferenceManager

class LocalDataSource(
    private val sharedPreferencesManager: SharedPreferenceManager
) {
    fun setAccessToken(token: String) {
        sharedPreferencesManager.saveString(USER_ACCESS_TOKEN_KEY, token)
    }

    fun getAccessToken() : String {
        return sharedPreferencesManager.getString(USER_ACCESS_TOKEN_KEY, "")
    }

    fun setRefreshToken(token: String) {
        sharedPreferencesManager.saveString(USER_REFRESH_TOKEN_KEY, token)
    }

    fun getRefreshToken() : String {
        return sharedPreferencesManager.getString(USER_REFRESH_TOKEN_KEY, "")
    }

    fun setLoginStatus(status: Boolean) {
        sharedPreferencesManager.saveBoolean(USER_LOGGED_IN_STATUS, status)
    }

    fun getLoginStatus() : Boolean {
        return sharedPreferencesManager.getBoolean(USER_LOGGED_IN_STATUS, false)
    }

    fun saveUsernameForRecovery(username: String) {
        sharedPreferencesManager.saveString(Constant.USERNAME_NASABAH, username)
    }

    fun getUsernameForRecovery() : String {
        return sharedPreferencesManager.getString(Constant.USERNAME_NASABAH, "")
    }

    fun setApplicationPin(pin: String) {
        return sharedPreferencesManager.saveString(USER_APPLICATION_PIN, pin)
    }

    fun getApplicationPin() : String {
        return sharedPreferencesManager.getString(USER_APPLICATION_PIN, "")
    }

    fun saveUserInfo(name: String, accountNum: String) {
        sharedPreferencesManager.saveString(Constant.NAMA_NASABAH, name)
        sharedPreferencesManager.saveString(Constant.NOMOR_REKENING_NASABAH, accountNum)
    }

    fun getUserInfo() : Pair<String, String> {
        val userName = sharedPreferencesManager.getString(Constant.NAMA_NASABAH, "")
        val userAccountNum = sharedPreferencesManager.getString(Constant.NOMOR_REKENING_NASABAH, "")

        return Pair(first = userName, second = userAccountNum)
    }

    fun clearTokenFromSharedPreferences() {
        sharedPreferencesManager.clearSharedPreferences(Constant.USER_ACCESS_TOKEN_KEY)
        sharedPreferencesManager.clearSharedPreferences(Constant.USER_REFRESH_TOKEN_KEY)
    }

    fun clearLoginInfo() {
        sharedPreferencesManager.clearSharedPreferences(Constant.USER_LOGGED_IN_STATUS)
        sharedPreferencesManager.clearSharedPreferences(Constant.USER_APPLICATION_PIN)
        sharedPreferencesManager.clearSharedPreferences(Constant.NOMOR_REKENING_NASABAH)
        sharedPreferencesManager.clearSharedPreferences(Constant.NAMA_NASABAH)
    }
}