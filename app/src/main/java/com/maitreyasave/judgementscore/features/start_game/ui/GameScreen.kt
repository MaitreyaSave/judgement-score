package com.maitreyasave.judgementscore.features.start_game.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.maitreyasave.judgementscore.features.add_player.PlayerViewModel
import com.maitreyasave.judgementscore.features.add_player.PlayerViewModelFactory
import com.maitreyasave.judgementscore.features.add_player.data.Player

@Composable
fun GameScreen(
    playerViewModel: PlayerViewModel = viewModel(factory = PlayerViewModelFactory(LocalContext.current))
) {
    val allPlayers by playerViewModel.players
    var numberOfPlayers by remember { mutableStateOf(0) }
    var selectedPlayers by remember { mutableStateOf<List<Player>>(emptyList()) }

    var showNumberDialog by remember { mutableStateOf(false) }
    var showPlayerPickerIndex by remember { mutableStateOf<Int?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Button(onClick = { showNumberDialog = true }) {
            Text("Set Number of Players")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (selectedPlayers.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                selectedPlayers.forEach { player ->
                    Text("${player.emoji} ${player.name}")
                }
            }
        }

        // Dialog to set number of players
        if (showNumberDialog) {
            SetNumberOfPlayersDialog(
                onDismiss = { showNumberDialog = false },
                onConfirm = { count ->
                    numberOfPlayers = count
                    selectedPlayers = List(count) { Player("", "") }
                    showNumberDialog = false
                    showPlayerPickerIndex = 0
                }
            )
        }

        // Player picker for each slot
        showPlayerPickerIndex?.let { index ->
            PlayerPickerDialog(
                allPlayers = allPlayers,
                onAddNew = { name, emoji ->
                    val newPlayer = Player(name, emoji)
                    playerViewModel.addPlayer(name, emoji)
                    selectedPlayers = selectedPlayers.toMutableList().also {
                        it[index] = newPlayer
                    }
                    if (index + 1 < numberOfPlayers) {
                        showPlayerPickerIndex = index + 1
                    } else {
                        showPlayerPickerIndex = null
                    }
                },
                onSelect = { player ->
                    selectedPlayers = selectedPlayers.toMutableList().also {
                        it[index] = player
                    }
                    if (index + 1 < numberOfPlayers) {
                        showPlayerPickerIndex = index + 1
                    } else {
                        showPlayerPickerIndex = null
                    }
                },
                onDismiss = { showPlayerPickerIndex = null }
            )
        }
    }
}
