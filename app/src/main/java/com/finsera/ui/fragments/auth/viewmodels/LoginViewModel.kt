package com.finsera.ui.fragments.auth.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.TYPE_ETHERNET
import android.net.ConnectivityManager.TYPE_WIFI
import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_ETHERNET
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import android.os.Build
import android.provider.ContactsContract.CommonDataKinds.Email.TYPE_MOBILE
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.finsera.MainApplication
import com.finsera.common.utils.Constant.Companion.USER_LOGGED_IN_STATUS
import com.finsera.common.utils.Resource
import com.finsera.common.utils.sharedpref.SharedPreferenceManager
import com.finsera.domain.usecase.auth.CheckLoggedInUseCase
import com.finsera.domain.usecase.auth.LoginUserUseCase
import com.finsera.ui.fragments.auth.uistate.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class LoginViewModel(
    app: Application,
    private val loginUserUseCase: LoginUserUseCase,
    private val checkLoggedInUseCase: CheckLoggedInUseCase
) : AndroidViewModel(app) {

        private val _loginScreenUIState = MutableStateFlow(LoginUiState())
    val loginScreenUIState = _loginScreenUIState.asStateFlow()

    private val _userLoggedInStatus = MutableLiveData<Boolean>()
    val userLoggedInStatus = _userLoggedInStatus

    init {
        checkUserLoggedInStatus()
    }

    fun userLogin(username: String, password: String) {
        viewModelScope.launch {
            if(hasInternetConnection()) {
                loginUserUseCase.invoke(username, password).collectLatest { result ->
                    when (result) {
                        is Resource.Loading -> {
                            _loginScreenUIState.update { uiState ->
                                uiState.copy(isLoading = true, message = null, isUserLoggedIn = false)
                            }
                        }
                        is Resource.Success -> {
                            _loginScreenUIState.update { uiState ->
                                uiState.copy(isLoading = false, message = result.data.toString(), isUserLoggedIn = true)
                            }
                        }
                        is Resource.Error -> {
                            _loginScreenUIState.update { uiState ->
                                uiState.copy(isLoading = false, message = result.message, isUserLoggedIn = false)
                            }
                        }
                    }
                }
            } else {
                _loginScreenUIState.update { uiState ->
                    uiState.copy(
                        isLoading = false,
                        message = "Tidak Ada Koneksi Internet",
                        isUserLoggedIn = false
                    )
                }
            }
        }
    }

    private fun checkUserLoggedInStatus()  {
        _userLoggedInStatus.value = checkLoggedInUseCase.invoke()
    }

    fun userMessageShown() {
        _loginScreenUIState.update { currentUiState ->
            currentUiState.copy(message = null)
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<MainApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when(type) {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}