package com.maitreyasave.judgementscore.features.start_game.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.maitreyasave.judgementscore.features.add_bet.BetAmountDialog
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

    // Grid logic
    var gameStarted by remember { mutableStateOf(false) }
    var numberOfRounds by remember { mutableStateOf(0) }
    var showRoundsDialog by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()
    val suitCellSize = 40.dp
    val buttonCellSize = 100.dp

    // State to manage the button position (either in the first row or second row)
    var buttonRowIndex by remember { mutableIntStateOf(0) }

    // State to track bet amounts for each player in the current row
    var betAmounts by remember { mutableStateOf<Map<Int, Map<Player, Int>>>(emptyMap()) }
    var showBetAmountDialog by remember { mutableStateOf(false) }
    var currentBetRow by remember { mutableStateOf(-1) }

    fun updateBetAmountsForRow(rowIndex: Int, betValues: Map<Player, Int>) {
        betAmounts = betAmounts.toMutableMap().apply {
            put(rowIndex, betValues)
        }
    }

    fun proceedToNextRow() {
        currentBetRow++
    }

    fun proceedToNextPlayer(currentIndex: Int) {
        showPlayerPickerIndex = if (currentIndex + 1 < numberOfPlayers) {
            currentIndex + 1
        } else {
            null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(24.dp)
    ) {
        // Row for buttons (Set Number of Players, Start Game)
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = { showNumberDialog = true },
                modifier = Modifier.weight(1f),
                enabled = !gameStarted
            ) {
                Text("Set Number of Players")
            }

            Button(
                onClick = {
                    gameStarted = true
                    showRoundsDialog = true
                },
                modifier = Modifier.weight(1f),
                enabled = selectedPlayers.size == numberOfPlayers && numberOfPlayers > 0 && !gameStarted
            ) {
                Text("Start Game")
            }
        }

        // When the game has started, show the grid
        if (gameStarted && numberOfRounds > 0) {
            val suits = listOf("♠️", "♥️", "♣️", "♦️", "⬜") // suit symbols

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
            ) {
                // Header Row
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Suit",
                        modifier = Modifier.width(suitCellSize),
                        textAlign = TextAlign.Center
                    )
                    selectedPlayers.forEach { player ->
                        Text(
                            text = "${player.emoji} ${player.name}",
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            maxLines = 1
                        )
                    }
                    Text("Bet", modifier = Modifier.width(buttonCellSize))
                    Text("Next", modifier = Modifier.width(buttonCellSize))
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Rounds Grid
                for (rowIndex in 0 until numberOfRounds) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        // First column = suits (cycled)
                        Text(
                            text = suits[rowIndex % suits.size],
                            modifier = Modifier
                                .width(suitCellSize)
                                .padding(start = 16.dp)
                        )

                        // Display the bet values from betAmounts
                        selectedPlayers.forEach { player ->
                            val bet = betAmounts[rowIndex]?.get(player) ?: 0
                            Text(
                                text = "$bet",
                                modifier = Modifier
                                    .weight(1f),
                                maxLines = 1,
                                // Center-align the text
                                softWrap = false,
                                textAlign = TextAlign.Center
                            )
                        }

                        // Button that will be placed in the last column of the specific row
                        if (rowIndex == buttonRowIndex) {
                            Button(
                                onClick = {
                                    showBetAmountDialog = true
                                    currentBetRow = rowIndex
                                },
                                modifier = Modifier.width(buttonCellSize)
                            ) {
                                Text("Bet")
                            }

                            Button(
                                onClick = {
                                    //
                                },
                                modifier = Modifier.width(buttonCellSize)
                            ) {
                                Text("Next")
                            }
                        } else {
                            Spacer(modifier = Modifier.width(buttonCellSize))
                            Spacer(modifier = Modifier.width(buttonCellSize))
                        }

                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Show selected players below
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
                selectedPlayers = selectedPlayers,
                onAddNew = { name, emoji ->
                    val newPlayer = Player(name, emoji)
                    playerViewModel.addPlayer(name, emoji)
                    selectedPlayers = selectedPlayers.toMutableList().also {
                        it[index] = newPlayer
                    }
                    proceedToNextPlayer(index)
                },
                onSelect = { player ->
                    selectedPlayers = selectedPlayers.toMutableList().also {
                        it[index] = player
                    }
                    proceedToNextPlayer(index)
                },
                onDismiss = { showPlayerPickerIndex = null }
            )
        }

        // Number of rounds
        if (showRoundsDialog) {
            var input by remember { mutableStateOf("") }

            AlertDialog(
                onDismissRequest = { showRoundsDialog = false },
                title = { Text("Enter Number of Rounds") },
                text = {
                    OutlinedTextField(
                        value = input,
                        onValueChange = { if (it.all { c -> c.isDigit() }) input = it },
                        label = { Text("Rounds") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            numberOfRounds = input.toIntOrNull() ?: 0
                            showRoundsDialog = false
                        }
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showRoundsDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }

        // Bet Amount Dialog
        if (showBetAmountDialog) {
            BetAmountDialog(
                players = selectedPlayers,
                initialBets = betAmounts[currentBetRow] ?: emptyMap(),
                onSaveBets = { betValues ->
                    updateBetAmountsForRow(currentBetRow, betValues)
                    proceedToNextRow()
                    showBetAmountDialog = false
                },
                onDismiss = { showBetAmountDialog = false }
            )
        }

    }
}
