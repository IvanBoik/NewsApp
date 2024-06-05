package com.boiko.newsapp.domain.usecases.auth

import com.boiko.newsapp.data.remote.auth.AuthResult
import com.boiko.newsapp.domain.repository.AuthRepository
import java.time.LocalDate

data class SignUpUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String, nickname: String, birthday: LocalDate): AuthResult<Unit> {
        return authRepository.signUp(
            email = email,
            password = password,
            nickname = nickname,
            birthday = birthday
        )
    }
}