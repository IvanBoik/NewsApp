package com.boiko.newsapp.data.remote.auth

import com.boiko.newsapp.data.remote.auth.dto.AuthResponse
import com.boiko.newsapp.data.remote.auth.dto.SignInRequest
import com.boiko.newsapp.data.remote.auth.dto.SignUpRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("signUp")
    suspend fun signUp(
        @Body request: SignUpRequest
    ): AuthResponse

    @POST("signIn")
    suspend fun signIn(
        @Body request: SignInRequest
    ): AuthResponse
}