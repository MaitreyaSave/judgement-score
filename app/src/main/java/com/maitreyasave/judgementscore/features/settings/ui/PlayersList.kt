package com.maitreyasave.judgementscore.features.settings.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.maitreyasave.judgementscore.features.settings.add_player.data.Player

@Composable
fun PlayersList(players: List<Player>) {
    Text("Players List:", style = MaterialTheme.typography.bodyLarge)
    players.forEach { player ->
        Text("${player.name} ${player.emoji}")
    }
}
