package com.thirfir.kreminder.di

import android.content.Context
import com.thirfir.data.datasource.local.KReminderDatabase
import com.thirfir.data.datasource.local.KeywordLocalDataSource
import com.thirfir.data.datasource.local.dao.KeywordDao
import com.thirfir.data.datasource.local.impl.KeywordLocalDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object LocalDataSourceModule {

    @Provides
    fun provideKeywordLocalDataSource(
        keywordDao: KeywordDao?
    ): KeywordLocalDataSource {
        return KeywordLocalDataSourceImpl(keywordDao)
    }

    @Provides
    fun provideKeywordDao(
        @ApplicationContext context: Context
    ) : KeywordDao? {
        return KReminderDatabase.getInstance(context)?.keywordDao()
    }
}