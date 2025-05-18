package com.maitreyasave.judgementscore.features.start_game

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import com.maitreyasave.judgementscore.features.start_game.data.GameState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class GameStateViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("game_prefs", Context.MODE_PRIVATE)

    private val _hasSavedGame = MutableStateFlow(false)
    val hasSavedGame: StateFlow<Boolean> = _hasSavedGame.asStateFlow()

    private val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }

    init {
        _hasSavedGame.value = prefs.getBoolean("is_game_in_progress", false)
    }

    fun markGameStarted() {
        prefs.edit { putBoolean("is_game_in_progress", true) }
        _hasSavedGame.value = true
    }

    fun markGameEnded() {
        prefs.edit {
            putBoolean("is_game_in_progress", false)
                .remove("game_state_json")
        }
        _hasSavedGame.value = false
    }

    fun saveGameState(gameState: GameState) {
        val jsonString = json.encodeToString(gameState)
        prefs.edit {
            putString("game_state_json", jsonString)
                .putBoolean("is_game_in_progress", true)
        }
        _hasSavedGame.value = true
    }

    fun loadGameState(): GameState? {
        val jsonString = prefs.getString("game_state_json", null) ?: return null
        return try {
            json.decodeFromString<GameState>(jsonString)
        } catch (e: Exception) {
            null
        }
    }
}