package com.boiko.newsapp.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.boiko.newsapp.data.local.NewsDAO
import com.boiko.newsapp.data.local.NewsDatabase
import com.boiko.newsapp.data.local.NewsTypeConverter
import com.boiko.newsapp.data.manager.LocalUserManagerImpl
import com.boiko.newsapp.data.remote.NewsApi
import com.boiko.newsapp.data.remote.UserApi
import com.boiko.newsapp.data.remote.auth.AuthApi
import com.boiko.newsapp.data.repository.AuthRepositoryImpl
import com.boiko.newsapp.data.repository.NewsRepositoryImpl
import com.boiko.newsapp.data.repository.UserRepositoryImpl
import com.boiko.newsapp.domain.manager.LocalUserManager
import com.boiko.newsapp.domain.repository.AuthRepository
import com.boiko.newsapp.domain.repository.NewsRepository
import com.boiko.newsapp.domain.repository.UserRepository
import com.boiko.newsapp.domain.usecases.app_entry.AppEntryUseCases
import com.boiko.newsapp.domain.usecases.app_entry.ReadAppEntry
import com.boiko.newsapp.domain.usecases.app_entry.SaveAppEntry
import com.boiko.newsapp.domain.usecases.auth.AuthUseCases
import com.boiko.newsapp.domain.usecases.auth.SignInUseCase
import com.boiko.newsapp.domain.usecases.auth.SignUpUseCase
import com.boiko.newsapp.domain.usecases.news.DeleteArticle
import com.boiko.newsapp.domain.usecases.news.GetNews
import com.boiko.newsapp.domain.usecases.news.NewsUseCases
import com.boiko.newsapp.domain.usecases.news.SearchNews
import com.boiko.newsapp.domain.usecases.news.SelectArticle
import com.boiko.newsapp.domain.usecases.news.SelectArticles
import com.boiko.newsapp.domain.usecases.news.UpsertArticle
import com.boiko.newsapp.domain.usecases.users.GetAvatar
import com.boiko.newsapp.domain.usecases.users.GetUserData
import com.boiko.newsapp.domain.usecases.users.UpdateAvatar
import com.boiko.newsapp.domain.usecases.users.UpdatePassword
import com.boiko.newsapp.domain.usecases.users.UpdateUserData
import com.boiko.newsapp.domain.usecases.users.UserUseCases
import com.boiko.newsapp.util.Constants.BASE_URL
import com.boiko.newsapp.util.Constants.NEWS_DATABASE_NAME
import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import java.time.LocalDate
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
    fun provideAuthUseCases(
        authRepository: AuthRepository
    ) = AuthUseCases(
        signInUseCase = SignInUseCase(authRepository),
        signUpUseCase = SignUpUseCase(authRepository)
    )

    @Provides
    @Singleton
    fun provideUserUseCases(
        prefs: SharedPreferences,
        userRepository: UserRepository
    ) = UserUseCases(
        getUserData = GetUserData(userRepository, prefs),
        getAvatar = GetAvatar(prefs),
        updateAvatar = UpdateAvatar(userRepository),
        updateUserData = UpdateUserData(userRepository),
        updatePassword = UpdatePassword(userRepository)
    )

    @Provides
    @Singleton
    fun provideOkHttpClient(prefs: SharedPreferences): OkHttpClient {
        val builder = OkHttpClient().newBuilder()
            .addInterceptor(Interceptor { chain ->
                val token = prefs.getString("jwt", "")
                val original = chain.request()
                if (original.url.pathSegments.contains("auth")) {
                    return@Interceptor chain.proceed(original)
                }
                val request = original.newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                chain.proceed(request)
            })
        return builder.build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(client: OkHttpClient): AuthApi {
        val moshi = Moshi.Builder()
            .add(LocaleDateAdapter)
            .build()
        return Retrofit.Builder()
            .baseUrl("http:10.0.2.2:8080/api/v1/auth/")
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideUserApi(client: OkHttpClient): UserApi {
        val moshi = Moshi.Builder()
            .add(LocaleDateAdapter)
            .build()
        return Retrofit.Builder()
            .baseUrl("http:10.0.2.2:8080/api/v1/users/")
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create()
    }

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
    fun provideAuthRepository(
        api: AuthApi,
        prefs: SharedPreferences
    ): AuthRepository {
        return AuthRepositoryImpl(api, prefs)
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        api: UserApi,
        prefs: SharedPreferences
    ): UserRepository {
        return UserRepositoryImpl(api, prefs)
    }

    @Provides
    @Singleton
    fun provideNewsRepository(
        newsApi: NewsApi,
        newsDAO: NewsDAO
    ): NewsRepository = NewsRepositoryImpl(newsApi, newsDAO)

    @Provides
    @Singleton
    fun provideNewsUseCases(
        newsRepository: NewsRepository
    ): NewsUseCases {
        return NewsUseCases(
            getNews = GetNews(newsRepository),
            searchNews = SearchNews(newsRepository),
            upsertArticle = UpsertArticle(newsRepository),
            deleteArticle = DeleteArticle(newsRepository),
            selectArticles = SelectArticles(newsRepository),
            selectArticle = SelectArticle(newsRepository)
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

    @Provides
    @Singleton
    fun provideSharedPref(app: Application): SharedPreferences {
        return app.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    }
}

object LocaleDateAdapter {
    @ToJson
    fun localDateToJson(localDate: LocalDate): String {
        return localDate.toString()
    }

    @FromJson
    fun jsonToLocalDate(json: String): LocalDate {
        return LocalDate.parse(json)
    }
}