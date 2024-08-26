package com.finsera.domain.usecase

import com.finsera.common.utils.Resource
import com.finsera.data.source.remote.ApiService
import com.finsera.domain.repository.FakeRemoteDataSource
import com.finsera.domain.repository.notifikasi.FakeNotifikasiRepository
import com.finsera.domain.usecase.notifikasi.NotifikasiUseCase
import com.finsera.domain.utils.FakeRetrofitBuilder
import kotlinx.coroutines.flow.collectLatest
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.File

class NotifikasiUseCaseUnitTest {
    private lateinit var apiService: ApiService
    private lateinit var mockWebServer: MockWebServer

    private lateinit var fakeRemoteDataSource: FakeRemoteDataSource
    private lateinit var fakeNotifikasiRepository: FakeNotifikasiRepository
    private lateinit var notifikasiUseCase: NotifikasiUseCase


    @Before
    fun setUp(){
        mockWebServer = MockWebServer().apply(MockWebServer::start)
        apiService = FakeRetrofitBuilder.generateRetrofit(mockWebServer).create(ApiService::class.java)

        fakeRemoteDataSource = FakeRemoteDataSource(apiService)
        fakeNotifikasiRepository= FakeNotifikasiRepository(fakeRemoteDataSource)
        notifikasiUseCase = NotifikasiUseCase(fakeNotifikasiRepository)
    }

    @Test
    fun `get notif return success when server return 200`(){
        runTest {

            val token = "Bearer token"
            val json = File("src/test/resources/NotificationSuccessful.json").readText()
            val mockResponse = MockResponse()
                .setBody(json)
                .setResponseCode(200)
                .addHeader("Content-Type","application/json")
                .addHeader("Authorization",token)

            mockWebServer.enqueue(mockResponse)

            notifikasiUseCase.invoke().collectLatest {
                when(it){
                    is Resource.Success->{
                        assertThat(it.data?.get(0)?.message).isEqualTo("Data notifkasi ditemukan")
                    }
                    is Resource.Error -> {

                    }

                    is Resource.Loading -> {

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