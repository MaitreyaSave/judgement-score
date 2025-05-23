package com.maitreyasave.judgementscore.features.home.start_game.data

import com.maitreyasave.judgementscore.features.settings.add_player.data.Player
import kotlinx.serialization.Serializable

@Serializable
data class GameState(
    val numberOfPlayers: Int,
    val selectedPlayers: List<Player>,
    val numberOfRounds: Int,
    val buttonRowIndex: Int,
    val currentBetRow: Int,
    val betAmounts: Map<Int, Map<String, Int>>,
    val maxHands: Int
)
