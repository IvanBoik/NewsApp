package com.boiko.newsapp.data.remote.dto

import java.time.LocalDate

data class UpdateUserDataRequest(
    val id: Long,
    val email: String,
    val nickname: String,
    val birthday: LocalDate
)