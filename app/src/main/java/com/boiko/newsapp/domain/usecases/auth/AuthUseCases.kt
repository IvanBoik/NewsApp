package com.boiko.newsapp.domain.usecases.auth

data class AuthUseCases(
    val signInUseCase: SignInUseCase,
    val signUpUseCase: SignUpUseCase
)
