package com.boiko.newsapp.domain.usecases.users

import android.content.SharedPreferences
import com.boiko.newsapp.data.remote.dto.UserData
import com.boiko.newsapp.domain.repository.UserRepository

data class GetUserData(
    private val userRepository: UserRepository,
    private val prefs: SharedPreferences
) {
    suspend operator fun invoke(): UserData {
        val id = prefs.getLong("id", 0)
        return userRepository.getUserData(id)
    }
}