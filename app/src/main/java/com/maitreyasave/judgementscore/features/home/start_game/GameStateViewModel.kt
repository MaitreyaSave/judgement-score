package com.maitreyasave.judgementscore.features.home.start_game

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewModelScope
import com.maitreyasave.judgementscore.database.GameHistoryDatabase
import com.maitreyasave.judgementscore.features.home.game_history.data.GameHistoryEntity
import com.maitreyasave.judgementscore.features.home.game_history.data.PlayerScoreEntity
import com.maitreyasave.judgementscore.features.home.start_game.data.GameState
import com.maitreyasave.judgementscore.features.settings.add_player.data.Player
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class GameStateViewModel(application: Application) : AndroidViewModel(application) {

    private val database = GameHistoryDatabase.getInstance(application)
    private val dao = database.gameHistoryDao()

    private val _gameHistory = MutableStateFlow<List<GameHistoryEntity>>(emptyList())
    val gameHistory: StateFlow<List<GameHistoryEntity>> = _gameHistory.asStateFlow()

    private val prefs = application.getSharedPreferences("game_prefs", Context.MODE_PRIVATE)
    private val _hasSavedGame = MutableStateFlow(false)
    val hasSavedGame: StateFlow<Boolean> = _hasSavedGame.asStateFlow()

    private val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }

    init {
        _hasSavedGame.value = prefs.getBoolean("is_game_in_progress", false)

        viewModelScope.launch(Dispatchers.IO) {
            dao.getAllGameHistoriesFlow().collect { _gameHistory.value = it }
        }
    }

    fun markGameStarted() {
        prefs.edit { putBoolean("is_game_in_progress", true) }
        _hasSavedGame.value = true
    }

    fun markGameEnded(players: List<Player>) {
        prefs.edit {
            putBoolean("is_game_in_progress", false)
            remove("game_state_json")
        }
        _hasSavedGame.value = false

        viewModelScope.launch(Dispatchers.IO) {
            val winner = players.maxByOrNull { it.score }?.name ?: "Unknown"
            val history = GameHistoryEntity(
                timestamp = System.currentTimeMillis(),
                winnerName = winner
            )

            val scores = players.map {
                PlayerScoreEntity(
                    gameId = 0, // to be set during insert
                    playerId = it.id,
                    playerName = it.name,
                    score = it.score
                )
            }

            dao.insertFullGame(history, scores)
        }
    }

    fun saveGameState(gameState: GameState) {
        val jsonString = json.encodeToString(gameState)
        prefs.edit {
            putString("game_state_json", jsonString)
            putBoolean("is_game_in_progress", true)
        }
        _hasSavedGame.value = true
    }

    fun loadGameState(): GameState? {
        val jsonString = prefs.getString("game_state_json", null) ?: return null
        return try {
            json.decodeFromString(jsonString)
        } catch (e: Exception) {
            null
        }
    }

    fun refreshGameStatus() {
        _hasSavedGame.value = prefs.getBoolean("is_game_in_progress", false)
    }

    fun deleteGameById(id: Int) {
        viewModelScope.launch {
            dao.deleteById(id)
        }
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
