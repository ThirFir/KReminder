package com.thirfir.domain.usecase

import com.thirfir.domain.model.Settings
import com.thirfir.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class SettingsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    fun getSettings(): Flow<Settings> {
        return settingsRepository.getSettings()
    }

    fun saveSettings(settings: Settings) {
        settingsRepository.saveSettings(settings)
    }

    fun getInitialSettings(): Settings =
        runBlocking {
            settingsRepository.getSettings().first()
        }
}