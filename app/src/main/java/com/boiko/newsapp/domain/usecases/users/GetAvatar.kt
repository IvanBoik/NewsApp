package com.boiko.newsapp.domain.usecases.users

import android.content.SharedPreferences
import com.boiko.newsapp.util.Constants.DEFAULT_AVATAR

data class GetAvatar(
    private val prefs: SharedPreferences
) {
    operator fun invoke(): String {
        return prefs.getString("avatar", DEFAULT_AVATAR)!!
    }
}