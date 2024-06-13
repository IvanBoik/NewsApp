package com.boiko.newsapp.data.remote

import com.boiko.newsapp.data.remote.dto.Song
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET

interface SongApi {
    @GET("all")
    suspend fun getAll(): List<Song>
}