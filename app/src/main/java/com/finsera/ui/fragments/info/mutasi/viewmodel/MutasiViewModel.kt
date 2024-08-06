package com.finsera.ui.fragments.info.mutasi.viewmodel

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
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finsera.MainApplication
import com.finsera.common.utils.Resource
import com.finsera.domain.model.Mutasi
import com.finsera.domain.usecase.mutasi.DownloadMutasiUseCase
import com.finsera.domain.usecase.mutasi.MutasiUseCase
import com.finsera.ui.fragments.info.mutasi.uistate.MutasiUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.ResponseBody

class MutasiViewModel(
    app: Application,
    private val mutasiUseCase: MutasiUseCase,
    private val downloadMutasiFileUseCase: DownloadMutasiUseCase
) : AndroidViewModel(app) {
    private val _mutasiUiState = MutableStateFlow(MutasiUiState())
    val mutasiUiState = _mutasiUiState.asStateFlow()

    fun getMutasi(startDate: String?, endDate: String?) {
        viewModelScope.launch {
            if(hasInternetConnection()) {
                mutasiUseCase.invoke(startDate, endDate).collectLatest { result ->
                    when (result) {
                        is Resource.Loading -> {
                            _mutasiUiState.update { uiState ->
                                uiState.copy(isLoading = true, message = null, mutasi = emptyList())
                            }
                        }

                        is Resource.Success -> {
                            _mutasiUiState.update { uiState ->
                                uiState.copy(isLoading = false, message = "Riwayat transaksi berhasil dimuat", mutasi = result.data ?: emptyList())
                            }
                        }

                        is Resource.Error -> {
                            _mutasiUiState.update { uiState ->
                                uiState.copy(isLoading = false, message = result.message, mutasi = emptyList())
                            }
                        }
                    }
                }
            } else {
                _mutasiUiState.update { uiState ->
                    uiState.copy(
                        isLoading = false,
                        mutasi = emptyList(),
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

    fun dataSubmittedToAdapter() {
        _mutasiUiState.update { currentUiState ->
            currentUiState.copy(mutasi = emptyList())
        }
    }

    fun messageShown() {
        _mutasiUiState.update { currentUiState ->
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