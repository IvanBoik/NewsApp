package com.boiko.newsapp.domain.usecases.auth

import com.boiko.newsapp.data.remote.auth.AuthResult
import com.boiko.newsapp.domain.repository.AuthRepository

data class SignInUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): AuthResult<Unit> {
        return authRepository.signIn(email = email, password = password)
    }
}