package com.maitreyasave.judgementscore.features.settings.shared

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


object SettingsRepository {

    private lateinit var dataStore: DataStore<Preferences>
    private val FEATURE_ENABLED_SHOULD_CALCULATE_ROUNDS_KEY = booleanPreferencesKey("feature_enabled_should_calculate_rounds")

    private val _shouldCalculateRoundsEnabled = MutableStateFlow(false)
    val shouldCalculateRoundsEnabled: StateFlow<Boolean> = _shouldCalculateRoundsEnabled.asStateFlow()

    fun init(dataStore: DataStore<Preferences>) {
        this.dataStore = dataStore

        CoroutineScope(Dispatchers.IO).launch {
            dataStore.data
                .map { prefs -> prefs[FEATURE_ENABLED_SHOULD_CALCULATE_ROUNDS_KEY] ?: false }
                .distinctUntilChanged()
                .collect {
                    _shouldCalculateRoundsEnabled.value = it
                }
        }
    }

    suspend fun setFeatureShouldCalculateRounds(value: Boolean) {
        dataStore.edit { prefs ->
            prefs[FEATURE_ENABLED_SHOULD_CALCULATE_ROUNDS_KEY] = value
        }
    }
}


