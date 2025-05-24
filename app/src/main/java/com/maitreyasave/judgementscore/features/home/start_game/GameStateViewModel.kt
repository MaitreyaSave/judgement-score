package com.maitreyasave.judgementscore.features.home.start_game

import android.app.Application
import android.content.Context
import android.util.Log
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class GameStateViewModel(application: Application) : AndroidViewModel(application) {

    private val database = GameHistoryDatabase.getInstance(application)
    private val dao = database.gameHistoryDao()

    val gameHistory: Flow<List<Pair<String, String>>> = dao.getAllGameHistoriesFlow()
        .map { games ->
            games.map { game ->
                val scores = dao.getScoresForGame(game.id)
                val scoreString = scores.joinToString("\n") {
                    "${it.playerName}: ${it.score}"
                }
                val formattedDate = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                    .format(Date(game.timestamp))
                "Game on $formattedDate - Winner: ${game.winnerName}" to scoreString
            }
        }.flowOn(Dispatchers.IO)

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
                    gameId = 0, // Will be updated in DAO
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