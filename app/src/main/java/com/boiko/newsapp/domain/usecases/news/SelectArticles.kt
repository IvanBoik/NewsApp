package com.boiko.newsapp.domain.usecases.news

import com.boiko.newsapp.domain.model.Article
import com.boiko.newsapp.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow

class SelectArticles(
    private val newsRepository: NewsRepository
) {
    operator fun invoke(): Flow<List<Article>> {
        return newsRepository.selectArticles()
    }
}