package com.boiko.newsapp.domain.usecases.users

import com.boiko.newsapp.domain.manager.LocalUserManager

data class LogOut(
    private val userManager: LocalUserManager
) {
    suspend operator fun invoke() {
        userManager.removeAppEntry()
    }
}