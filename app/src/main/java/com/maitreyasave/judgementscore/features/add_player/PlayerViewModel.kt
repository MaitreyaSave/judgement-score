package com.maitreyasave.judgementscore.features.add_player


import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maitreyasave.judgementscore.features.add_player.data.Player
import com.maitreyasave.judgementscore.features.add_player.data.PlayerRepository
import kotlinx.coroutines.launch

class PlayerViewModel(private val repository: PlayerRepository) : ViewModel() {

    private val _players = mutableStateOf<List<Player>>(emptyList())
    val players: State<List<Player>> = _players

    init {
        loadPlayers()
    }

    private fun loadPlayers() {
        _players.value = repository.loadPlayers()
    }

    fun addPlayer(name: String, emoji: String) {
        val newPlayer = Player(name, emoji)
        val updated = _players.value + newPlayer
        _players.value = updated

        viewModelScope.launch {
            repository.savePlayers(updated)
        }
    }
}

