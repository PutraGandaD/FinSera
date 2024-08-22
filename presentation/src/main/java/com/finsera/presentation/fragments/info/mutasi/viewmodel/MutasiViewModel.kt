package com.finsera.presentation.fragments.info.mutasi.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finsera.common.utils.Resource
import com.finsera.common.utils.network.ConnectivityManager
import com.finsera.domain.usecase.auth.GetUserInfoUseCase
import com.finsera.domain.usecase.mutasi.DownloadMutasiUseCase
import com.finsera.domain.usecase.mutasi.MutasiUseCase
import com.finsera.presentation.fragments.info.mutasi.uistate.MutasiUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.ResponseBody

class MutasiViewModel(
    private val connectivityManager: ConnectivityManager,
    private val mutasiUseCase: MutasiUseCase,
    private val downloadMutasiFileUseCase: DownloadMutasiUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase
) : ViewModel() {
    private val _mutasiUiState = MutableStateFlow(MutasiUiState())
    val mutasiUiState = _mutasiUiState.asStateFlow()

    private var _userInfo : Pair<String, String>? = null
    val userInfo : Pair<String, String>?
        get() = _userInfo

    init {
        getUserInfo()
    }

    fun getMutasi(startDate: String?, endDate: String?, page: Int) {
        viewModelScope.launch {
            if(connectivityManager.hasInternetConnection()) {
                mutasiUseCase.invoke(startDate, endDate, page).collectLatest { result ->
                    when (result) {
                        is Resource.Loading -> {
                            _mutasiUiState.update { uiState ->
                                uiState.copy(isLoading = true, isSuccess = false, isError = false, message = null, mutasi = null)
                            }
                        }

                        is Resource.Success -> {
                            _mutasiUiState.update { uiState ->
                                uiState.copy(isLoading = false, isSuccess = true, isError = false, message = "Riwayat transaksi berhasil dimuat", mutasi = result.data)
                            }
                        }

                        is Resource.Error -> {
                            _mutasiUiState.update { uiState ->
                                uiState.copy(isLoading = false, isSuccess = false, isError = true, message = result.message, mutasi = null)
                            }
                        }
                    }
                }
            } else {
                _mutasiUiState.update { uiState ->
                    uiState.copy(
                        isLoading = false,
                        isSuccess = false,
                        isError = false,
                        mutasi = null,
                        message = "Tidak ada koneksi Internet."
                    )
                }
            }

        }
    }

    private val _downloadState = MutableLiveData<Resource<ResponseBody>>()
    val downloadState: LiveData<Resource<ResponseBody>> = _downloadState

    fun downloadMutasiFile(startDate: String, endDate: String) {
        viewModelScope.launch {
            _downloadState.postValue(Resource.Loading())
            try {
                downloadMutasiFileUseCase(startDate, endDate).collect { resource ->
                    _downloadState.postValue(resource)
                }
            } catch (t: Throwable) {
                _downloadState.postValue(Resource.Error("An unexpected error occurred"))
            }
        }
    }

    fun resetUiState() {
        _mutasiUiState.update { currentUiState ->
            currentUiState.copy(
                isLoading = false,
                isSuccess = false,
                isError = false,
                mutasi = null,
                message = null
            )
        }
    }

    fun messageShown() {
        _mutasiUiState.update { currentUiState ->
            currentUiState.copy(message = null)
        }
    }

    private fun getUserInfo() {
        _userInfo = getUserInfoUseCase.invoke()
    }
}