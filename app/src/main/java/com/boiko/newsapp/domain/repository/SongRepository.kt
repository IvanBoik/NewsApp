package com.boiko.newsapp.domain.repository

import com.boiko.newsapp.data.remote.dto.Song
import kotlinx.coroutines.flow.Flow

interface SongRepository {
    suspend fun getAll(): List<Song>
}