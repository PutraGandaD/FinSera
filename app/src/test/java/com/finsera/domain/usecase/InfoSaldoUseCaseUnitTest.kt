package com.finsera.domain.usecase

import com.finsera.common.utils.Resource
import com.finsera.data.source.remote.ApiService
import com.finsera.domain.repository.FakeAuthRepository
import com.finsera.domain.repository.FakeRemoteDataSource
import com.finsera.domain.repository.infosaldo.FakeInfoSaldoRepository
import com.finsera.domain.usecase.infosaldo.InfoSaldoUseCase
import com.finsera.domain.utils.FakeRetrofitBuilder
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Test
import java.io.File

class InfoSaldoUseCaseUnitTest {

    private lateinit var apiService: ApiService
    private lateinit var mockWebServer: MockWebServer

    private lateinit var fakeRemoteDataSource: FakeRemoteDataSource
    private lateinit var fakeAuthRepository: FakeAuthRepository
    private lateinit var fakeInfoSaldoRepository: FakeInfoSaldoRepository
    private lateinit var infoSaldoUseCase: InfoSaldoUseCase


    @Before
    fun setUp(){
        mockWebServer = MockWebServer().apply(MockWebServer::start)
        apiService = FakeRetrofitBuilder.generateRetrofit(mockWebServer).create(ApiService::class.java)

        fakeRemoteDataSource = FakeRemoteDataSource(apiService)
        fakeAuthRepository = FakeAuthRepository(fakeRemoteDataSource)
        fakeInfoSaldoRepository = FakeInfoSaldoRepository(fakeRemoteDataSource)
        infoSaldoUseCase = InfoSaldoUseCase(fakeInfoSaldoRepository,fakeAuthRepository)

    }


    @Test
    fun `get saldo return success when server return 200`(){
        runTest {
            val token = "Bearer token"
            val json = File("src/test/resources/InfoSaldoSuccessful.json").readText()
            val mockResponse = MockResponse()
                .setBody(json)
                .setResponseCode(200)
                .addHeader("Content-Type","application/json")
                .addHeader("Authorization",token)

            mockWebServer.enqueue(mockResponse)

            fakeAuthRepository.getUserInfo()

            infoSaldoUseCase.invoke().collectLatest {
                when(it){
                    is Resource.Success->{
                        assertThat(it.data?.accountNumber).isEqualTo("123456789")
                    }
                    is Resource.Loading->{

                    }
                    is Resource.Error -> {

                    }
                }
            }
        }
    }
}