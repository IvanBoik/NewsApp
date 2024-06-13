package com.boiko.newsapp.presentation.music_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boiko.newsapp.domain.usecases.songs.GetSongs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
data class MusicViewModel @Inject constructor(
    private val getSongs: GetSongs
): ViewModel() {

    private val _state = mutableStateOf(MusicState())
    val state: State<MusicState> = _state

    init {
        getAll()
    }

    private fun getAll() {
        viewModelScope.launch {
            _state.value = _state.value.copy(songs = getSongs())
        }
    }
}