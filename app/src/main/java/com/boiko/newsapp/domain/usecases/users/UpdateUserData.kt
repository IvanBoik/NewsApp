package com.boiko.newsapp.domain.usecases.users

import com.boiko.newsapp.domain.repository.UserRepository
import java.time.LocalDate

data class UpdateUserData(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(email: String, nickname: String, birthday: LocalDate) {
        userRepository.updateUserData(email, nickname, birthday)
    }
}