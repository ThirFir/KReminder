package com.thirfir.kreminder.di

import com.thirfir.domain.IoDispatcher
import com.thirfir.domain.repository.KeywordRepository
import com.thirfir.domain.repository.PostHeaderRepository
import com.thirfir.domain.repository.PostRepository
import com.thirfir.domain.usecase.KeywordUseCase
import com.thirfir.domain.usecase.GetPostHeaderUseCase
import com.thirfir.domain.usecase.GetPostUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope

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

    @Provides
    fun provideKeywordUseCase(
        keywordRepository: KeywordRepository,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
    ) : KeywordUseCase {
        return KeywordUseCase(keywordRepository, ioDispatcher)
    }
}