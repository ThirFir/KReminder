package com.thirfir.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thirfir.domain.model.Settings
import com.thirfir.domain.usecase.SettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsUseCase: SettingsUseCase
) : ViewModel() {

    private val _settings: MutableStateFlow<Settings> =
        MutableStateFlow(settingsUseCase.getInitialSettings())
    val settings: StateFlow<Settings> get() = _settings.asStateFlow()

    init {
        getSettings()
    }

    private fun getSettings() {
        viewModelScope.launch {
            settingsUseCase.getSettings().collect {
                _settings.emit(it)
            }
        }
    }

    fun updateSettings(action: (Settings) -> Unit) {
        action(_settings.value)
        settingsUseCase.saveSettings(_settings.value)
    }
}