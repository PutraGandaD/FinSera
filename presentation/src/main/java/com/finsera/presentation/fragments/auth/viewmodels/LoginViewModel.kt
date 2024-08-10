package com.finsera.presentation.fragments.auth.viewmodels

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finsera.common.utils.Constant.Companion.USER_LOGGED_IN_STATUS
import com.finsera.common.utils.Resource
import com.finsera.common.utils.network.ConnectivityManager
import com.finsera.common.utils.sharedpref.SharedPreferenceManager
import com.finsera.domain.usecase.auth.CheckLoggedInUseCase
import com.finsera.domain.usecase.auth.LoginUserUseCase
import com.finsera.presentation.fragments.auth.uistate.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

class LoginViewModel(
    private val connectivityManager: ConnectivityManager,
    private val loginUserUseCase: LoginUserUseCase,
    private val checkLoggedInUseCase: CheckLoggedInUseCase
) : ViewModel() {
    private val _loginScreenUIState = MutableStateFlow(LoginUiState())
    val loginScreenUIState = _loginScreenUIState.asStateFlow()

    private val _userLoggedInStatus = MutableLiveData<Boolean>()
    val userLoggedInStatus = _userLoggedInStatus

    init {
        checkUserLoggedInStatus()
    }

    fun userLogin(username: String, password: String) {
        viewModelScope.launch {
            if(connectivityManager.hasInternetConnection()) {
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
}