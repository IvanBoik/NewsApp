package com.boiko.newsapp.di

import android.app.Application
import androidx.room.Room
import com.boiko.newsapp.data.local.NewsDAO
import com.boiko.newsapp.data.local.NewsDatabase
import com.boiko.newsapp.data.local.NewsTypeConverter
import com.boiko.newsapp.data.manager.LocalUserManagerImpl
import com.boiko.newsapp.data.remote.NewsApi
import com.boiko.newsapp.data.repository.NewsRepositoryImpl
import com.boiko.newsapp.domain.manager.LocalUserManager
import com.boiko.newsapp.domain.repository.NewsRepository
import com.boiko.newsapp.domain.usecases.app_entry.AppEntryUseCases
import com.boiko.newsapp.domain.usecases.app_entry.ReadAppEntry
import com.boiko.newsapp.domain.usecases.app_entry.SaveAppEntry
import com.boiko.newsapp.domain.usecases.news.GetNews
import com.boiko.newsapp.domain.usecases.news.NewsUseCases
import com.boiko.newsapp.domain.usecases.news.SearchNews
import com.boiko.newsapp.util.Constants.BASE_URL
import com.boiko.newsapp.util.Constants.NEWS_DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLocalUserManager(
        application: Application
    ) : LocalUserManager = LocalUserManagerImpl(application)

    @Provides
    @Singleton
    fun provideAppEntryUseCases(
        localUserManager: LocalUserManager
    ) = AppEntryUseCases(
        readAppEntry = ReadAppEntry(localUserManager),
        saveAppEntry = SaveAppEntry(localUserManager)
    )

    @Provides
    @Singleton
    fun provideNewsApi(): NewsApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideNewsRepository(
        newsApi: NewsApi
    ): NewsRepository = NewsRepositoryImpl(newsApi)

    @Provides
    @Singleton
    fun provideNewsUseCases(
        newsRepository: NewsRepository
    ): NewsUseCases {
        return NewsUseCases(
            getNews = GetNews(newsRepository),
            searchNews = SearchNews(newsRepository)
        )
    }

    @Provides
    @Singleton
    fun provideNewsDatabase(
        application: Application
    ): NewsDatabase {
        return Room.databaseBuilder(
            context = application,
            klass = NewsDatabase::class.java,
            name = NEWS_DATABASE_NAME
        ).addTypeConverter(NewsTypeConverter())
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideNewsDAO(
        newsDatabase: NewsDatabase
    ): NewsDAO = newsDatabase.newsDAO
}