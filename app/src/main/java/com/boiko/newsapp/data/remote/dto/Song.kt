package com.boiko.newsapp.data.remote.dto

data class Song(
    val audioURL: String,
    val imageURL: String,
    val name: String,
    val author: String,
    val duration: Long
)