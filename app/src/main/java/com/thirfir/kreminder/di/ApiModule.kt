package com.thirfir.kreminder.di

import com.thirfir.domain.DefaultDispatcher
import com.thirfir.domain.IoDispatcher
import com.thirfir.domain.MainDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @IoDispatcher
    fun provideIoDispatcher() : CoroutineDispatcher = Dispatchers.IO

    @Provides
    @MainDispatcher
    fun provideMainDispatcher() : CoroutineDispatcher = Dispatchers.Main

    @Provides
    @DefaultDispatcher
    fun provideDefaultDispatcher() : CoroutineDispatcher = Dispatchers.Default

    @Provides
    fun provideIoScope(
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ) : CoroutineScope = CoroutineScope(ioDispatcher)

    @Provides
    fun provideMainScope(
        @MainDispatcher mainDispatcher: CoroutineDispatcher
    ) : CoroutineScope = CoroutineScope(mainDispatcher)

    @Provides
    fun provideDefaultScope(
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ) : CoroutineScope = CoroutineScope(defaultDispatcher)
}