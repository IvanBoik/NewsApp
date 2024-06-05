package com.boiko.newsapp.data.remote

import com.boiko.newsapp.data.remote.dto.UserData
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import java.time.LocalDate

interface UserApi {
    @GET("/{id}")
    suspend fun getUserData(@Path("id") id: Long): UserData

    @PUT("/data")
    suspend fun updateUserData(email: String, nickname: String, birthday: LocalDate)

    @PUT("/password")
    suspend fun updatePassword(id: Long, password: String, repeatPassword: String)

    @PUT("/avatar")
    @Multipart
    suspend fun updateAvatar(id: Long, @Part("avatar") image: MultipartBody.Part)
}