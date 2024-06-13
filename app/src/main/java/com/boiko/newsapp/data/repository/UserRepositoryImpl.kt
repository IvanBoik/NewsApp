package com.boiko.newsapp.data.repository

import android.content.SharedPreferences
import android.util.Log
import com.boiko.newsapp.data.remote.UserApi
import com.boiko.newsapp.data.remote.dto.UpdatePasswordRequest
import com.boiko.newsapp.data.remote.dto.UpdateUserDataRequest
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
        try {
            val userData = userApi.getUserData(id)
            Log.d("test", userData.toString())
            return userData
        }
        catch (e: Exception) {
            Log.d("test", e.toString())
            throw e
        }
    }

    override suspend fun updateUserData(email: String, nickname: String, birthday: LocalDate) {
        val id = prefs.getLong("id", 0)
        userApi.updateUserData(UpdateUserDataRequest(id, email, nickname, birthday))
    }

    override suspend fun updatePassword(oldPassword: String, newPassword: String) {
        val id = prefs.getLong("id", 0)
        val password = prefs.getString("password", "")
        if (oldPassword == password) {
            userApi.updatePassword(UpdatePasswordRequest(id, newPassword))
            prefs.edit().putString("password", newPassword).apply()
        }
        else {
            throw RuntimeException()
        }
    }

    override suspend fun updateAvatar(avatar: File): String {
        val id = prefs.getLong("id", 0)
        val multipartBody = MultipartBody.Part.createFormData("avatar", "avatar",
            avatar.asRequestBody("image/*".toMediaTypeOrNull())
        )
        return userApi.updateAvatar(id, multipartBody).avatar
    }
}