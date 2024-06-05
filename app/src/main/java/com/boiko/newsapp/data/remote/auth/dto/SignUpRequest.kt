package com.boiko.newsapp.data.remote.auth.dto

import java.time.LocalDate

data class SignUpRequest(
    val email: String,
    val password: String,
    val nickname: String,
    val birthday: LocalDate
)
