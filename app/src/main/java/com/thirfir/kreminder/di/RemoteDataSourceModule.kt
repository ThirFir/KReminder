package com.thirfir.kreminder.di

import com.thirfir.data.datasource.remote.PostHeaderRemoteDataSource
import com.thirfir.data.datasource.remote.PostRemoteDataSource
import com.thirfir.data.datasource.remote.impl.PostHeaderRemoteDataSourceImpl
import com.thirfir.data.datasource.remote.impl.PostRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RemoteDataSourceModule {
    @Provides
    fun providePostHeaderRemoteDataSource(): PostHeaderRemoteDataSource {
        return PostHeaderRemoteDataSourceImpl()
    }

    @Provides
    fun providePostRemoteDataSource(): PostRemoteDataSource {
        return PostRemoteDataSourceImpl()
    }
}