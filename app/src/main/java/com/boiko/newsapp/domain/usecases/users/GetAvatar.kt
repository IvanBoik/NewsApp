package com.boiko.newsapp.domain.usecases.users

import android.content.SharedPreferences
import android.util.Log
import com.boiko.newsapp.util.Constants.DEFAULT_AVATAR

data class GetAvatar(
    private val prefs: SharedPreferences
) {
    operator fun invoke(): String {
        val avatar = prefs.getString("avatar", DEFAULT_AVATAR)!!
        Log.d("test", avatar)
        return avatar
    }
}