package com.maitreyasave.judgementscore.features.start_game

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.maitreyasave.judgementscore.features.start_game.data.GameState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class GameStateViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("game_prefs", Context.MODE_PRIVATE)

    private val _hasSavedGame = MutableStateFlow(false)
    val hasSavedGame: StateFlow<Boolean> = _hasSavedGame.asStateFlow()

    private val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }

    init {
        _hasSavedGame.value = prefs.getBoolean("is_game_in_progress", false)
        Log.d("GameStateViewModel", "Initial hasSavedGame: ${_hasSavedGame.value}")
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
        Log.d("GameStateViewModel", "Initial hasSavedGame: ${_hasSavedGame.value}")
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
    fun refreshGameStatus() {
        _hasSavedGame.value = prefs.getBoolean("is_game_in_progress", false)
    }


    companion object {
        fun getInstance(application: Application): GameStateViewModel {
            val storeOwner = application as ViewModelStoreOwner
            return ViewModelProvider(
                storeOwner,
                ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            )[GameStateViewModel::class.java]
        }
    }

}