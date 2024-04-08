package com.boiko.newsapp.domain.usecases.news

import com.boiko.newsapp.domain.model.Article
import com.boiko.newsapp.domain.repository.NewsRepository

class SelectArticle(
    private val newsRepository: NewsRepository
) {
    suspend operator fun invoke(url: String): Article? {
        return newsRepository.selectArticle(url)
    }
}