package com.thirfir.kreminder.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.thirfir.data.SettingsProto
import com.thirfir.data.datasource.local.settings.serializer.SettingsSerializer
import com.thirfir.domain.IoDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Provides
    fun provideSettingsSerializer(): SettingsSerializer {
        return SettingsSerializer
    }

    @Provides
    @Singleton
    fun providesSettingsDataStore(
        @ApplicationContext context: Context,
        settingsSerializer: SettingsSerializer,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): DataStore<SettingsProto> =
        DataStoreFactory.create(
            serializer = settingsSerializer,
            produceFile = { context.dataStoreFile("settings.pb") },
            scope = CoroutineScope( ioDispatcher + SupervisorJob())
        )
}