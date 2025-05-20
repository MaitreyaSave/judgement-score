package com.maitreyasave.judgementscore.features.home.start_game.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.maitreyasave.judgementscore.features.settings.add_player.data.Player

@Composable
fun PlayerScoresRow(players: List<Player>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        players.forEach { player ->
            Text(
                text = "${player.emoji} ${player.name}: ${player.score}",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
        }
    }
}
