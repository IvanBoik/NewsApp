package com.boiko.newsapp.data.repository

import android.content.SharedPreferences
import com.boiko.newsapp.data.remote.UserApi
import com.boiko.newsapp.data.remote.dto.UserData
import com.boiko.newsapp.domain.repository.UserRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.time.LocalDate

class UserRepositoryImpl(
    private val userApi: UserApi,
    private val prefs: SharedPreferences
): UserRepository {
    override suspend fun getUserData(id: Long): UserData {
        return userApi.getUserData(id)
    }

    override suspend fun updateUserData(email: String, nickname: String, birthday: LocalDate) {
        userApi.updateUserData(email, nickname, birthday)
    }

    override suspend fun updatePassword(password: String, repeatPassword: String) {
        val id = prefs.getLong("id", 0)
        userApi.updatePassword(id, password, repeatPassword)
    }

    override suspend fun updateAvatar(avatar: File) {
        val id = prefs.getLong("id", 0)
        val multipartBody = MultipartBody.Part.createFormData("avatar", "avatar",
            avatar.asRequestBody("image/*".toMediaTypeOrNull())
        )
        userApi.updateAvatar(id, multipartBody)
    }
}