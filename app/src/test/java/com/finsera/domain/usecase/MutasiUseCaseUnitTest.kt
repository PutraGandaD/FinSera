package com.finsera.domain.usecase

import com.finsera.common.utils.Resource
import com.finsera.data.source.remote.ApiService
import com.finsera.domain.repository.FakeAuthRepository
import com.finsera.domain.repository.FakeRemoteDataSource
import com.finsera.domain.repository.mutasi.FakeMutasiRepository
import com.finsera.domain.usecase.mutasi.MutasiUseCase
import com.finsera.domain.utils.FakeRetrofitBuilder
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.File

class MutasiUseCaseUnitTest {
    private lateinit var apiService: ApiService
    private lateinit var mockWebServer: MockWebServer

    private lateinit var fakeRemoteDataSource: FakeRemoteDataSource
    private lateinit var fakeAuthRepository: FakeAuthRepository
    private lateinit var fakeMutasiRepository: FakeMutasiRepository
    private lateinit var mutasiUseCase: MutasiUseCase

    @Before
    fun setUp(){
        mockWebServer = MockWebServer().apply(MockWebServer::start)
        apiService = FakeRetrofitBuilder.generateRetrofit(mockWebServer).create(ApiService::class.java)

        fakeRemoteDataSource = FakeRemoteDataSource(apiService)
        fakeMutasiRepository = FakeMutasiRepository(fakeRemoteDataSource)
        fakeAuthRepository = FakeAuthRepository(fakeRemoteDataSource)
        mutasiUseCase = MutasiUseCase(fakeAuthRepository,fakeMutasiRepository)
    }

    @Test
    fun `get mutasi return success when server return 200`(){
        runTest {
            val token = "Bearer token"
            val json = File("src/test/resources/MutasiSuccessful.json").readText()
            val mockResponse = MockResponse()
                .setBody(json)
                .setResponseCode(200)
                .addHeader("Content-Type","application/json")
                .addHeader("Authorization",token)

            mockWebServer.enqueue(mockResponse)

            mutasiUseCase.invoke("","",10).collectLatest {
                when(it){
                    is Resource.Success->{
                        assertThat(it.data).isNotNull()
                    }
                    is Resource.Loading->{

                    }
                    is Resource.Error -> {

                    }
                }
            }
        }
    }

    @After
    fun shutdown() {
        mockWebServer.shutdown()
    }

}