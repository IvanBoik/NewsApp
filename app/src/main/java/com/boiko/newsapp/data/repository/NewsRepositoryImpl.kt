package com.boiko.newsapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.boiko.newsapp.data.local.NewsDAO
import com.boiko.newsapp.data.remote.NewsApi
import com.boiko.newsapp.data.remote.NewsPagingSource
import com.boiko.newsapp.data.remote.SearchNewsPagingSource
import com.boiko.newsapp.domain.model.Article
import com.boiko.newsapp.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow

class NewsRepositoryImpl(
    private val newsApi: NewsApi,
    private val newsDAO: NewsDAO
): NewsRepository {
    override fun getNews(sources: List<String>): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = {
                NewsPagingSource(
                    newsApi = newsApi,
                    sources = sources.joinToString(separator = ",")
                )
            }
        ).flow
    }

    override fun searchNews(searchQuery: String, sources: List<String>): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = {
                SearchNewsPagingSource(
                    searchQuery = searchQuery,
                    newsApi = newsApi,
                    sources = sources.joinToString(separator = ",")
                )
            }
        ).flow
    }

    override suspend fun upsertArticle(article: Article) {
        newsDAO.upsert(article)
    }

    override suspend fun deleteArticle(article: Article) {
        newsDAO.delete(article)
    }

    override fun selectArticles(): Flow<List<Article>> {
        return newsDAO.getArticles()
    }

    override suspend fun selectArticle(url: String): Article? {
        return newsDAO.getArticle(url)
    }
}