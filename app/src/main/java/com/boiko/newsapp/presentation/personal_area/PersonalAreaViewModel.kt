package com.boiko.newsapp.presentation.personal_area

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boiko.newsapp.data.remote.dto.UserData
import com.boiko.newsapp.domain.usecases.users.UserUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
data class PersonalAreaViewModel @Inject constructor(
    private val userUseCases: UserUseCases,
    private val prefs: SharedPreferences
): ViewModel() {

    private val _bitmap = MutableStateFlow<Bitmap?>(null)
    val bitmap = _bitmap.asStateFlow()

    fun onTakePhoto(context: Context, bitmap: Bitmap?) {
        _bitmap.value = bitmap
        if (bitmap != null) {
            updateAvatar(context)
        }
    }

    fun onEvent(event: PersonalAreaEvent) {
        var exception: Exception? = null

        when(event) {
            is PersonalAreaEvent.UpdateDataEvent -> {
                viewModelScope.launch {
                    userUseCases.updateUserData(
                        email = event.email,
                        nickname = event.nickname,
                        birthday = event.birthday
                    )
                }
            }

            is PersonalAreaEvent.UpdatePasswordEvent -> {
                runBlocking {
                    try {
                        userUseCases.updatePassword(
                            oldPassword = event.oldPassword,
                            newPassword = event.newPassword
                        )
                    }
                    catch (e: RuntimeException) {
                        exception = e
                    }
                }
            }

            is PersonalAreaEvent.UpdateAvatarEvent -> {
                viewModelScope.launch {
                    userUseCases.updateAvatar(
                        context = event.context,
                        avatar = event.avatar
                    )
                }
            }

            is PersonalAreaEvent.LogOutEvent -> {
                runBlocking {
                    userUseCases.logOut()
                }
            }
        }

        if (exception != null) {
            throw exception as Exception
        }
    }

    fun getUserData(): UserData {
        var userData: UserData?
        runBlocking {
            userData = userUseCases.getUserData()
        }
        return userData!!
    }

    fun getAvatar(): String {
        return userUseCases.getAvatar()
    }

    fun updateAvatar(context: Context): String {
        var avatarURL: String?
        runBlocking {
            try {
                avatarURL = userUseCases.updateAvatar(
                    context = context,
                    avatar = bitmap.value!!
                )
            }
            catch (e: Exception) {
                Log.d("test", e.toString())
                throw e
            }
        }
        prefs.edit().putString("avatar", avatarURL).apply()
        return avatarURL!!
    }
}