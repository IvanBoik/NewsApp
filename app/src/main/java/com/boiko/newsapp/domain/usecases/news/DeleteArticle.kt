package com.boiko.newsapp.domain.usecases.news

import com.boiko.newsapp.domain.model.Article
import com.boiko.newsapp.domain.repository.NewsRepository

class DeleteArticle(
    private val newsRepository: NewsRepository
) {
    suspend operator fun invoke(article: Article) {
        newsRepository.deleteArticle(article)
    }
}