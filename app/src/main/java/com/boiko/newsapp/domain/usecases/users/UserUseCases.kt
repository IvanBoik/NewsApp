package com.boiko.newsapp.domain.usecases.users

data class UserUseCases(
    val getUserData: GetUserData,
    val getAvatar: GetAvatar,
    val updateAvatar: UpdateAvatar,
    val updateUserData: UpdateUserData,
    val updatePassword: UpdatePassword,
    val logOut: LogOut
)