package com.thirfir.kreminder.di

import com.thirfir.data.datasource.remote.PostHeaderRemoteDataSource
import com.thirfir.data.datasource.remote.PostRemoteDataSource
import com.thirfir.data.datasource.remote.impl.PostHeaderRemoteDataSourceImpl
import com.thirfir.data.datasource.remote.impl.PostRemoteDataSourceImpl
import com.thirfir.domain.IoDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(SingletonComponent::class)
object RemoteDataSourceModule {
    @Provides
    fun providePostHeaderRemoteDataSource(
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): PostHeaderRemoteDataSource {
        return PostHeaderRemoteDataSourceImpl(ioDispatcher)
    }

    @Provides
    fun providePostRemoteDataSource(): PostRemoteDataSource {
        return PostRemoteDataSourceImpl()
    }
}