package com.boiko.newsapp.domain.usecases.songs

import com.boiko.newsapp.data.remote.dto.Song
import com.boiko.newsapp.domain.repository.SongRepository
import kotlinx.coroutines.flow.Flow

data class GetSongs(
    private val songRepository: SongRepository
) {
    suspend operator fun invoke(): List<Song> {
        return songRepository.getAll()
    }
}
