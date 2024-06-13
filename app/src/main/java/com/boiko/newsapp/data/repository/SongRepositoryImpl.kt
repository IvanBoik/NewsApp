package com.boiko.newsapp.data.repository

import android.util.Log
import com.boiko.newsapp.data.remote.SongApi
import com.boiko.newsapp.data.remote.dto.Song
import com.boiko.newsapp.domain.repository.SongRepository
import kotlinx.coroutines.flow.Flow

class SongRepositoryImpl(
    private val songApi: SongApi
): SongRepository {
    override suspend fun getAll(): List<Song> {
        try {
            return songApi.getAll()
        }
        catch (e: Exception) {
            Log.d("test2", e.toString())
            throw e
        }
    }
}