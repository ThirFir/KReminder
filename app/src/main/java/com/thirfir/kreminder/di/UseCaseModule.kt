package com.thirfir.kreminder.di

import com.thirfir.domain.repository.PostHeaderRepository
import com.thirfir.domain.repository.PostRepository
import com.thirfir.domain.usecase.GetPostHeaderUseCase
import com.thirfir.domain.usecase.GetPostUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideGetPostHeaderUseCase(
        postHeaderRepository: PostHeaderRepository
    ): GetPostHeaderUseCase {
        return GetPostHeaderUseCase(postHeaderRepository)
    }

    @Provides
    fun provideGetPostUseCase(
        postRepository: PostRepository
    ): GetPostUseCase {
        return GetPostUseCase(postRepository)
    }
}