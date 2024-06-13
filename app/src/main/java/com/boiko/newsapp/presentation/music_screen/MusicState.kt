package com.boiko.newsapp.presentation.music_screen

import com.boiko.newsapp.data.remote.dto.Song

data class MusicState(
    val songs: List<Song> = emptyList()
)