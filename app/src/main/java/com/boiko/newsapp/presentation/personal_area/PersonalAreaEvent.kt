package com.boiko.newsapp.presentation.personal_area

import android.content.Context
import android.graphics.Bitmap
import java.time.LocalDate

sealed class PersonalAreaEvent {
    data class UpdateDataEvent(val email: String, val nickname: String, val birthday: LocalDate): PersonalAreaEvent()
    data class UpdatePasswordEvent(val oldPassword: String, val newPassword: String): PersonalAreaEvent()
    data class UpdateAvatarEvent(val context: Context, val avatar: Bitmap): PersonalAreaEvent()

    data object LogOutEvent: PersonalAreaEvent()
}