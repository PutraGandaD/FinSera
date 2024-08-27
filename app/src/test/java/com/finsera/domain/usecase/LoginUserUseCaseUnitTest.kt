package com.finsera.domain.usecase

import com.finsera.common.utils.Resource
import com.finsera.data.source.remote.ApiService
import com.finsera.domain.repository.FakeAuthRepository
import com.finsera.domain.repository.FakeRemoteDataSource
import com.finsera.domain.usecase.auth.LoginUserUseCase
import com.finsera.domain.utils.FakeRetrofitBuilder
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import java.io.File

class LoginUserUseCaseUnitTest {
    private lateinit var apiService: ApiService
    private lateinit var mockWebServer: MockWebServer

    private lateinit var fakeRemoteDataSource: FakeRemoteDataSource
    private lateinit var fakeAuthRepository: FakeAuthRepository
    private lateinit var loginUserUseCase: LoginUserUseCase

    @Before
    fun setUp() {

        mockWebServer = MockWebServer().apply(MockWebServer::start)
        apiService = FakeRetrofitBuilder.generateRetrofit(mockWebServer).create(ApiService::class.java)

        fakeRemoteDataSource = FakeRemoteDataSource(apiService)
        fakeAuthRepository = FakeAuthRepository(fakeRemoteDataSource)
        loginUserUseCase = LoginUserUseCase(fakeAuthRepository)
    }

    @Test
    fun `login return success when server return 200 ok`() {
        runTest {
            val json = File("src/test/resources/UserLoginSuccessful.json").readText()
            val mockResponse = MockResponse()
                .setBody(json)
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json")

            mockWebServer.enqueue(mockResponse)

            loginUserUseCase.invoke("johndoe", "password123").collectLatest { result ->
                when(result) {
                    is Resource.Success -> {
                        assertThat(result.data).isEqualTo("Login Berhasil")
                    }

                    is Resource.Error -> {

                    }

                    is Resource.Loading -> {

                    }
                }
            }
        }
    }

    @Test
    fun `login return error when server return 401 unauthorized (wrong username or password)`() {
        runTest {
            val json = File("src/test/resources/UserLoginFailedUnauthorized.json").readText()
            val mockResponse = MockResponse()
                .setBody(json)
                .setResponseCode(401)
                .addHeader("Content-Type", "application/json")

            mockWebServer.enqueue(mockResponse)

            loginUserUseCase.invoke("johndoe", "password1234").collectLatest { result ->
                when(result) {
                    is Resource.Success -> {

                    }

                    is Resource.Error -> {
                        assertThat(result.message).isEqualTo("Username atau password yang anda masukkan salah!")
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