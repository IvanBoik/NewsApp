package com.boiko.newsapp.presentation.personal_area

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boiko.newsapp.data.remote.dto.UserData
import com.boiko.newsapp.domain.usecases.users.UserUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
data class PersonalAreaViewModel @Inject constructor(
    private val userUseCases: UserUseCases
): ViewModel() {

    fun onEvent(event: PersonalAreaEvent) {
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
                viewModelScope.launch {
                    userUseCases.updatePassword(
                        password = event.password,
                        repeatPassword = event.repeatPassword
                    )
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
        }
    }

    fun getUserData(): UserData {
        var userData: UserData? = null
        runBlocking {
            userData = userUseCases.getUserData()
        }
        return userData!!
    }
}