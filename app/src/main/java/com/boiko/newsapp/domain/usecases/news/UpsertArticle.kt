package com.boiko.newsapp.domain.usecases.news

import com.boiko.newsapp.data.local.NewsDAO
import com.boiko.newsapp.domain.model.Article

class UpsertArticle(
    private val newsDAO: NewsDAO
) {
    suspend operator fun invoke(article: Article) {
        newsDAO.upsert(article)
    }
}