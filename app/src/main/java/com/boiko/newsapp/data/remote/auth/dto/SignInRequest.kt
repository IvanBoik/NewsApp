package com.boiko.newsapp.data.remote.auth.dto

data class SignInRequest(
    val email: String,
    val password: String
)