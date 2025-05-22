package com.maitreyasave.judgementscore.features.settings.calculate_rounds

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maitreyasave.judgementscore.features.settings.shared.SettingsRepository
import kotlinx.coroutines.launch

class ShouldCalculateRoundsViewModel : ViewModel() {
    val isFeatureEnabled = SettingsRepository.shouldCalculateRoundsEnabled

    fun onFeatureEnabledChanged(enabled: Boolean) {
        viewModelScope.launch {
            SettingsRepository.setFeatureShouldCalculateRounds(enabled)
        }
    }
}