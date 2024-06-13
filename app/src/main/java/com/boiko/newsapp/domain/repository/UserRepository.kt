package com.boiko.newsapp.domain.repository

import com.boiko.newsapp.data.remote.dto.AvatarResponse
import com.boiko.newsapp.data.remote.dto.UserData
import java.io.File
import java.time.LocalDate

interface UserRepository {
    suspend fun getUserData(id: Long): UserData
    suspend fun updateUserData(email: String, nickname: String, birthday: LocalDate)
    suspend fun updatePassword(oldPassword: String, newPassword: String)
    suspend fun updateAvatar(avatar: File): String
}