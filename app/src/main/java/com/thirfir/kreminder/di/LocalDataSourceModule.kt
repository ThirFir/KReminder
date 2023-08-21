package com.thirfir.kreminder.di

import android.content.Context
import androidx.datastore.core.DataStore
import com.thirfir.data.SettingsProto
import com.thirfir.data.datasource.local.keyword.KReminderDatabase
import com.thirfir.data.datasource.local.keyword.KeywordLocalDataSource
import com.thirfir.data.datasource.local.keyword.dao.KeywordDao
import com.thirfir.data.datasource.local.keyword.impl.KeywordLocalDataSourceImpl
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
}