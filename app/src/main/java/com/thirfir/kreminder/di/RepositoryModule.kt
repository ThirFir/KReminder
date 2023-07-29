package com.thirfir.kreminder.di

import com.thirfir.data.datasource.remote.PostHeaderRemoteDataSource
import com.thirfir.data.datasource.remote.PostRemoteDataSource
import com.thirfir.data.repository.PostHeaderRepositoryImpl
import com.thirfir.data.repository.PostRepositoryImpl
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
    ): PostHeaderRepositoryImpl {
        return PostHeaderRepositoryImpl(postHeaderRemoteDataSource)
    }

    @Provides
    fun providePostRepository(
        postRemoteDataSource: PostRemoteDataSource
    ): PostRepositoryImpl {
        return PostRepositoryImpl(postRemoteDataSource)
    }

}