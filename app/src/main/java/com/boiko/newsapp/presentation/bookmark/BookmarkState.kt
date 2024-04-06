package com.boiko.newsapp.presentation.bookmark

import com.boiko.newsapp.domain.model.Article

data class BookmarkState(
    val articles: List<Article> = emptyList()
)