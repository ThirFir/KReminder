package com.thirfir.kreminder.di

import android.content.Context
import androidx.datastore.core.DataStore
import com.thirfir.data.SettingsProto
import com.thirfir.data.datasource.local.BookmarkLocalDataSource
import com.thirfir.data.datasource.local.KReminderDatabase
import com.thirfir.data.datasource.local.KeywordLocalDataSource
import com.thirfir.data.datasource.local.dao.BookmarkDao
import com.thirfir.data.datasource.local.dao.KeywordDao
import com.thirfir.data.datasource.local.impl.BookmarkLocalDataSourceImpl
import com.thirfir.data.datasource.local.impl.KeywordLocalDataSourceImpl
import com.thirfir.data.datasource.local.settings.SettingsLocalDataSource
import com.thirfir.data.datasource.local.settings.impl.SettingsLocalDataSourceImpl
import com.thirfir.domain.IoDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher

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
    ) : KeywordDao {
        return KReminderDatabase.getInstance(context).keywordDao()
    }

    @Provides
    fun provideSettingsLocalDataSource(
        settingsDataStore: DataStore<SettingsProto>,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ) : SettingsLocalDataSource {
        return SettingsLocalDataSourceImpl(settingsDataStore, ioDispatcher)
    }

    @Provides
    fun provideBookmarkLocalDataSource(
        bookmarkDao: BookmarkDao?
    ): BookmarkLocalDataSource {
        return BookmarkLocalDataSourceImpl(bookmarkDao)
    }

    @Provides
    fun provideBookmarkDao(
        @ApplicationContext context: Context
    ) : BookmarkDao {
        return KReminderDatabase.getInstance(context).bookmarkDao()
    }

}