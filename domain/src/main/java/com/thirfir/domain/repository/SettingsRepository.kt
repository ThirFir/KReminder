package com.thirfir.domain.repository

import com.thirfir.domain.model.Settings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getSettings(): Flow<Settings>

    fun saveSettings(settings: Settings)
}