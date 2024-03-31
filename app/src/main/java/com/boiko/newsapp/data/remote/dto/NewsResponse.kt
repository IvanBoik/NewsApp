package com.boiko.newsapp.data.remote.dto

import com.boiko.newsapp.domain.model.Article

data class NewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)