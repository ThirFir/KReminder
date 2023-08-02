package com.thirfir.kreminder.di

import com.thirfir.data.datasource.remote.PostHeaderRemoteDataSource
import com.thirfir.data.datasource.remote.PostRemoteDataSource
import com.thirfir.data.repository.PostHeaderRepositoryImpl
import com.thirfir.data.repository.PostRepositoryImpl
import com.thirfir.domain.repository.PostHeaderRepository
import com.thirfir.domain.repository.PostRepository
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

}