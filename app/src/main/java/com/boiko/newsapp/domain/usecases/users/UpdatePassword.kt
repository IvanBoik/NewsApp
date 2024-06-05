package com.boiko.newsapp.domain.usecases.users

import com.boiko.newsapp.domain.repository.UserRepository

data class UpdatePassword(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(password: String, repeatPassword: String) {
        userRepository.updatePassword(password, repeatPassword)
    }
}