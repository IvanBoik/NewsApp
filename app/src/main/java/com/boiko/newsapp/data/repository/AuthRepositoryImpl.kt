package com.boiko.newsapp.data.repository

import android.content.SharedPreferences
import com.boiko.newsapp.data.remote.auth.AuthApi
import com.boiko.newsapp.data.remote.auth.AuthResult
import com.boiko.newsapp.data.remote.auth.dto.SignInRequest
import com.boiko.newsapp.data.remote.auth.dto.SignUpRequest
import com.boiko.newsapp.domain.repository.AuthRepository
import retrofit2.HttpException
import java.time.LocalDate

class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val prefs: SharedPreferences
): AuthRepository {
    override suspend fun signUp(
        email: String,
        password: String,
        nickname: String,
        birthday: LocalDate
    ): AuthResult<Unit> {
        return try {
            val response = authApi.signUp(
                request = SignUpRequest(
                    email = email,
                    password = password,
                    nickname = nickname,
                    birthday = birthday
                )
            )
            prefs.edit()
                .remove("jwt")
                .putString("jwt", response.token)
                .putLong("id", response.id)
                .putString("avatar", response.avatarURL)
                .putString("password", password)
                .apply()

            AuthResult.Authorized()
        } catch (e: HttpException) {
            if (e.code() == 401 || e.code() == 403) {
                AuthResult.Unauthorized()
            } else {
                AuthResult.UnknownError()
            }
        } catch (e: Exception) {
            AuthResult.UnknownError()
        }
    }

    override suspend fun signIn(email: String, password: String): AuthResult<Unit> {
        return try {
            val response = authApi.signIn(
                request = SignInRequest(
                    email = email,
                    password = password
                )
            )
            prefs.edit()
                .remove("jwt")
                .putString("jwt", response.token)
                .putLong("id", response.id)
                .putString("avatar", response.avatarURL)
                .putString("password", password)
                .apply()
            AuthResult.Authorized()
        } catch (e: HttpException) {
            if (e.code() == 401 || e.code() == 403) {
                AuthResult.Unauthorized()
            } else {
                AuthResult.UnknownError()
            }
        } catch (e: Exception) {
            AuthResult.UnknownError()
        }
    }
}