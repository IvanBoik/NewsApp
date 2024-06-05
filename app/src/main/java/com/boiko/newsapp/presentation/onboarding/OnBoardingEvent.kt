package com.boiko.newsapp.presentation.onboarding

import java.time.LocalDate

sealed class OnBoardingEvent {

    data class SignUp(val email: String, val password: String, val nickname: String, val birthday: LocalDate): OnBoardingEvent()

    data class SignIn(val email: String, val password: String): OnBoardingEvent()
    data object SaveAppEntry: OnBoardingEvent()
}