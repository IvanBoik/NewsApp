package com.boiko.newsapp.data.remote.dto

data class UpdatePasswordRequest(
    val id: Long,
    val newPassword: String
)