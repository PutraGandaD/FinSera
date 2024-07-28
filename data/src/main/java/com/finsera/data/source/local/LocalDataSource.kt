package com.finsera.data.source.local

import com.finsera.common.utils.Constant.Companion.USER_TOKEN_KEY
import com.finsera.common.utils.sharedpref.SharedPreferenceManager

class LocalDataSource(
    private val sharedPreferencesManager: SharedPreferenceManager
) {
    fun setUserToken(token: String) {
        sharedPreferencesManager.saveString(USER_TOKEN_KEY, token)
    }

    fun getUserToken() : String {
        return sharedPreferencesManager.getString(USER_TOKEN_KEY, "")
    }
}