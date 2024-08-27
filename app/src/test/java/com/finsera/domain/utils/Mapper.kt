package com.finsera.domain.utils

import com.finsera.data.source.remote.response.login.LoginResponse
import com.finsera.domain.model.Login

object Mapper {
    fun loginDataToDomain(response: LoginResponse): Login {
        return Login(
            code = response.code,
            message = response.message,
            accessToken = response.data?.accessToken,
            refreshToken = response.data?.refreshToken,
            status = response.data?.status,
            userId = response.data?.userId
        )
    }
}