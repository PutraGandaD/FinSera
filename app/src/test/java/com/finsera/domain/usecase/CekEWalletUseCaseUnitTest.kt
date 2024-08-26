package com.finsera.domain.usecase

import com.finsera.common.utils.Resource
import com.finsera.data.source.remote.ApiService
import com.finsera.domain.repository.FakeAuthRepository
import com.finsera.domain.repository.FakeRemoteDataSource
import com.finsera.domain.repository.transfer.FakeTransferRepository
import com.finsera.domain.usecase.transfer.ewallet.CekEWalletUseCase
import com.finsera.domain.utils.FakeRetrofitBuilder
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Test
import java.io.File

class CekEWalletUseCaseUnitTest {
    private lateinit var apiService: ApiService
    private lateinit var mockWebServer: MockWebServer

    private lateinit var fakeRemoteDataSource: FakeRemoteDataSource
    private lateinit var fakeAuthRepository: FakeAuthRepository
    private lateinit var fakeTransferRepository: FakeTransferRepository
    private lateinit var cekEWalletUseCase: CekEWalletUseCase


    @Before
    fun setUp(){
        mockWebServer = MockWebServer().apply(MockWebServer::start)
        apiService = FakeRetrofitBuilder.generateRetrofit(mockWebServer).create(ApiService::class.java)

        fakeRemoteDataSource = FakeRemoteDataSource(apiService)
        fakeAuthRepository = FakeAuthRepository(fakeRemoteDataSource)
        fakeTransferRepository = FakeTransferRepository(fakeRemoteDataSource)
        cekEWalletUseCase = CekEWalletUseCase(fakeTransferRepository)
    }

    @Test
    fun `cek e-wallet return success when server return 200`(){
        runTest {
            val token = "Bearer token"
            val json = File("src/test/resources/CekEWalletSuccessful.json").readText()
            val mockResponse = MockResponse()
                .setBody(json)
                .setResponseCode(200)
                .addHeader("Content-Type","application/json")
                .addHeader("Authorization",token)

            mockWebServer.enqueue(mockResponse)
            cekEWalletUseCase.invoke(1,"089123123123").collectLatest {
                when(it){
                    is Resource.Success->{
                        assertThat(it.data).isNotNull()
                        assertThat(it.data?.namaEwallet).isEqualTo("DANA")
                        assertThat(it.data?.message).isEqualTo("Akun e-wallet ditemukan")
                    }
                    is Resource.Error->{

                    }
                    is Resource.Loading->{
                    }
                }
            }

        }

    }
}