package com.thirfir.kreminder.di

import com.thirfir.data.datasource.local.keyword.KeywordLocalDataSource
import com.thirfir.data.datasource.local.settings.SettingsLocalDataSource
import com.thirfir.data.datasource.remote.PostHeaderRemoteDataSource
import com.thirfir.data.datasource.remote.PostRemoteDataSource
import com.thirfir.data.repository.KeywordRepositoryImpl
import com.thirfir.data.repository.PostHeaderRepositoryImpl
import com.thirfir.data.repository.PostRepositoryImpl
import com.thirfir.data.repository.SettingsRepositoryImpl
import com.thirfir.domain.repository.KeywordRepository
import com.thirfir.domain.repository.PostHeaderRepository
import com.thirfir.domain.repository.PostRepository
import com.thirfir.domain.repository.SettingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun providePostHeaderRepository(
        postHeaderRemoteDataSource: PostHeaderRemoteDataSource
    ): PostHeaderRepository {
        return PostHeaderRepositoryImpl(postHeaderRemoteDataSource)
    }

    @Provides
    fun providePostRepository(
        postRemoteDataSource: PostRemoteDataSource
    ): PostRepository {
        return PostRepositoryImpl(postRemoteDataSource)
    }

    @Provides
    fun provideKeywordRepository(
        keywordLocalDataSource: KeywordLocalDataSource
    ): KeywordRepository {
        return KeywordRepositoryImpl(keywordLocalDataSource)
    }

    @Provides
    fun provideSettingsRepository(
        settingsLocalDataSource: SettingsLocalDataSource
    ): SettingsRepository {
        return SettingsRepositoryImpl(settingsLocalDataSource)
    }
}