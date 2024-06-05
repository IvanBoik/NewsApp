package com.boiko.newsapp.domain.repository

import com.boiko.newsapp.data.remote.auth.AuthResult
import java.time.LocalDate

interface AuthRepository {
    suspend fun signUp(
        email: String,
        password: String,
        nickname: String,
        birthday: LocalDate
    ): AuthResult<Unit>

    suspend fun signIn(email: String, password: String): AuthResult<Unit>
}