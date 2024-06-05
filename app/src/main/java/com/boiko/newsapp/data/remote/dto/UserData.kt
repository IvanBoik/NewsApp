package com.boiko.newsapp.data.remote.dto

import java.time.LocalDate

data class UserData(
    val email: String,
    val nickname: String,
    val passwordLength: Int,
    val birthday: LocalDate
)