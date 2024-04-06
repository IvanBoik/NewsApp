package com.boiko.newsapp.domain.usecases.news

import com.boiko.newsapp.data.local.NewsDAO
import com.boiko.newsapp.domain.model.Article
import kotlinx.coroutines.flow.Flow

class SelectArticles(
    private val newsDAO: NewsDAO
) {
    operator fun invoke(): Flow<List<Article>> {
        return newsDAO.getArticles()
    }
}