package com.thirfir.kreminder.di

import com.thirfir.data.datasource.remote.PostHeaderRemoteDataSource
import com.thirfir.data.datasource.remote.PostRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RemoteDataModule {
    @Provides
    fun providePostHeaderRemoteDataSource(): PostHeaderRemoteDataSource {
        return PostHeaderRemoteDataSource()
    }

    @Provides
    fun providePostRemoteDataSource(): PostRemoteDataSource {
        return PostRemoteDataSource()
    }
}