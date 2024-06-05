package com.boiko.newsapp.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boiko.newsapp.data.remote.auth.AuthResult
import com.boiko.newsapp.domain.usecases.app_entry.AppEntryUseCases
import com.boiko.newsapp.domain.usecases.auth.AuthUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val authUseCases: AuthUseCases,
    private val appEntryUseCases: AppEntryUseCases
) : ViewModel() {

    private val resultChannel = Channel<AuthResult<Unit>>()
    val authResults = resultChannel.receiveAsFlow()

    fun onEvent(event: OnBoardingEvent) {
        when(event) {
            is OnBoardingEvent.SignUp -> {
                signUp(
                    email = event.email,
                    password = event.password,
                    nickname = event.nickname,
                    birthday = event.birthday
                )
            }
            is OnBoardingEvent.SignIn -> {
                signIn(
                    email = event.email,
                    password = event.password
                )
            }

            is OnBoardingEvent.SaveAppEntry -> {
                saveAppEntry()
            }
        }
    }

    private fun signUp(email: String, password: String, nickname: String, birthday: LocalDate) {
        viewModelScope.launch {
            val result = authUseCases.signUpUseCase(
                email = email,
                password = password,
                nickname = nickname,
                birthday = birthday
            )
            resultChannel.send(result)
        }
    }

    private fun signIn(email: String, password: String) {
        viewModelScope.launch {
            val result = authUseCases.signInUseCase(
                email = email,
                password = password
            )
            resultChannel.send(result)
        }
    }

    private fun saveAppEntry() {
        viewModelScope.launch {
            appEntryUseCases.saveAppEntry()
        }
    }
}