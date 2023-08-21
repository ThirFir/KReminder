package com.thirfir.data.datasource.local.settings

import com.thirfir.data.SettingsProto
import com.thirfir.domain.model.Settings
import kotlinx.coroutines.flow.Flow

interface SettingsLocalDataSource {
    fun getSettings(): Flow<SettingsProto>

    fun setSettings(settings: Settings)
}