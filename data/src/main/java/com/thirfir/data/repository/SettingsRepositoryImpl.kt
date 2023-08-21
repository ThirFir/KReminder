package com.thirfir.data.repository

import com.thirfir.data.datasource.local.settings.SettingsLocalDataSource
import com.thirfir.data.toSettings
import com.thirfir.domain.model.Settings
import com.thirfir.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val settingsLocalDataSource: SettingsLocalDataSource
) : SettingsRepository {
    override fun getSettings(): Flow<Settings> =
        settingsLocalDataSource.getSettings().map { it.toSettings() }

    override fun saveSettings(settings: Settings) =
        settingsLocalDataSource.setSettings(settings)

}