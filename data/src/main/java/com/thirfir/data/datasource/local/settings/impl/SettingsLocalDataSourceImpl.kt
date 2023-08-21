package com.thirfir.data.datasource.local.settings.impl

import androidx.datastore.core.DataStore
import com.thirfir.data.SettingsProto
import com.thirfir.data.datasource.local.settings.SettingsLocalDataSource
import com.thirfir.domain.IoDispatcher
import com.thirfir.domain.model.Settings
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

class SettingsLocalDataSourceImpl @Inject constructor(
    private val settingsDataStore: DataStore<SettingsProto>,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val ioScope: CoroutineScope = CoroutineScope(ioDispatcher)
) : SettingsLocalDataSource {

    override fun getSettings(): Flow<SettingsProto> = settingsDataStore.data
        .catch { exception ->
            if ( exception is IOException ) {
                emit(SettingsProto.getDefaultInstance())
            } else {
                throw exception
            }
        }

    override fun setSettings(settings: Settings) {
        ioScope.launch {
            settingsDataStore.updateData { proto ->
                proto.toBuilder()
                    .setAllowNotification(settings.allowNotification)
                    .build()
            }
        }
    }
}