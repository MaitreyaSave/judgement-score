package com.maitreyasave.judgementscore.features.start_game.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TopGameButtons(
    enabled: Boolean,
    canStart: Boolean,
    onSetPlayers: () -> Unit,
    onStartGame: () -> Unit,
    onEndGame: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(onClick = onSetPlayers, enabled = enabled, modifier = Modifier.weight(1f)) {
            Text("Set Number of Players")
        }
        Button(onClick = onStartGame, enabled = canStart && enabled, modifier = Modifier.weight(1f)) {
            Text("Start Game")
        }
        Button(onClick = onEndGame, enabled = !enabled, modifier = Modifier.weight(1f)) {
            Text("End Game")
        }
    }
}
