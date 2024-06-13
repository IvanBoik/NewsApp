package com.boiko.newsapp.data.remote

import com.boiko.newsapp.data.remote.dto.AvatarResponse
import com.boiko.newsapp.data.remote.dto.UpdatePasswordRequest
import com.boiko.newsapp.data.remote.dto.UpdateUserDataRequest
import com.boiko.newsapp.data.remote.dto.UserData
import com.boiko.newsapp.domain.usecases.users.UpdateAvatar
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface UserApi {
    @GET("{id}")
    suspend fun getUserData(@Path("id") id: Long): UserData

    @PUT("data")
    suspend fun updateUserData(@Body request: UpdateUserDataRequest)

    @PUT("password")
    suspend fun updatePassword(@Body request: UpdatePasswordRequest)

    @PUT("avatar")
    @Multipart
    suspend fun updateAvatar(@Part("id") id: Long, @Part avatar: MultipartBody.Part): AvatarResponse
}