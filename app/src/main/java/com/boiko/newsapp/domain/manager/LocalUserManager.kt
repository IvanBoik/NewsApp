package com.boiko.newsapp.domain.manager

import kotlinx.coroutines.flow.Flow

interface LocalUserManager {
    suspend fun saveAppEntry()

    suspend fun removeAppEntry()

    fun readAppEntry() : Flow<Boolean>
}