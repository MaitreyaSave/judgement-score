package com.maitreyasave.judgementscore.features.home.start_game.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
    onEndGame: () -> Unit,
    isPortrait: Boolean
) {
    val arrangement = Arrangement.spacedBy(12.dp)

    if (isPortrait) {
        Column(
            verticalArrangement = arrangement,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = onSetPlayers,
                enabled = enabled,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Set Number of Players")
            }
            Button(
                onClick = onStartGame,
                enabled = canStart && enabled,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Start Game")
            }
            Button(
                onClick = onEndGame,
                enabled = !enabled,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("End Game")
            }
        }
    } else {
        Row(
            horizontalArrangement = arrangement,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = onSetPlayers,
                enabled = enabled,
                modifier = Modifier.weight(1f)
            ) {
                Text("Set Number of Players")
            }
            Button(
                onClick = onStartGame,
                enabled = canStart && enabled,
                modifier = Modifier.weight(1f)
            ) {
                Text("Start Game")
            }
            Button(
                onClick = onEndGame,
                enabled = !enabled,
                modifier = Modifier.weight(1f)
            ) {
                Text("End Game")
            }
        }
    }
}

