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
import com.maitreyasave.judgementscore.features.select_winner.WinnerSelectionDialog

@Composable
fun GameScreen(
    playerViewModel: PlayerViewModel = viewModel(factory = PlayerViewModelFactory(LocalContext.current))
) {
    // These states come from your existing PlayerViewModel or your own source.
    // For this example, we assume selectedPlayers are managed locally.
    var numberOfPlayers by remember { mutableIntStateOf(0) }
    var selectedPlayers by remember { mutableStateOf<List<Player>>(emptyList()) }

    var showNumberDialog by remember { mutableStateOf(false) }
    var showPlayerPickerIndex by remember { mutableStateOf<Int?>(null) }

    // Grid/Game logic state kept directly in GameScreen:
    var gameStarted by remember { mutableStateOf(false) }
    var numberOfRounds by remember { mutableIntStateOf(0) }
    var showRoundsDialog by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()
    val suitCellSize = 40.dp
    val buttonCellSize = 100.dp

    // Which row is currently active for showing the "Bet" button
    var buttonRowIndex by remember { mutableIntStateOf(0) }

    // Map of round index to bet values (Player -> bet amount)
    var betAmounts by remember { mutableStateOf<Map<Int, Map<Player, Int>>>(emptyMap()) }
    var showBetAmountDialog by remember { mutableStateOf(false) }
    var currentBetRow by remember { mutableIntStateOf(0) }

    // Winner selection dialog state
    var showWinnerDialog by remember { mutableStateOf(false) }

    // Helper function: update bet amounts for a given round.
    fun updateBetAmountsForRow(rowIndex: Int, betValues: Map<Player, Int>) {
        betAmounts = betAmounts.toMutableMap().apply {
            put(rowIndex, betValues)
        }
    }


    // Proceed to the next round (active round index).
    fun proceedToNextRow() {
        currentBetRow++
        buttonRowIndex++
    }

    // Proceed to next player slot when picking players (if applicable).
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
        // Top buttons: Set number of players, Start game
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

        // Grid: Display rounds if game started and rounds are set.
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
                    Text("Bet", modifier = Modifier.width(buttonCellSize), textAlign = TextAlign.Center)
                    Text("Next", modifier = Modifier.width(buttonCellSize), textAlign = TextAlign.Center)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Rounds Grid: For each round, display suit and bet values, and show buttons in the active row.
                for (rowIndex in 0 until numberOfRounds) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        // Suit column for this round.
                        Text(
                            text = suits[rowIndex % suits.size],
                            modifier = Modifier
                                .width(suitCellSize)
                                .padding(start = 16.dp),
                            textAlign = TextAlign.Center
                        )
                        // For each player, display the bet value from betAmounts; default to 0.
                        selectedPlayers.forEach { player ->
                            val bet = betAmounts[rowIndex]?.get(player) ?: 0
                            Text(
                                text = "$bet",
                                modifier = Modifier.weight(1f),
                                maxLines = 1,
                                softWrap = false,
                                textAlign = TextAlign.Center
                            )
                        }
                        // Place active row buttons only in the row matching buttonRowIndex.
                        if (rowIndex == buttonRowIndex) {
                            Button(
                                onClick = {
                                    showBetAmountDialog = true
                                },
                                modifier = Modifier.width(buttonCellSize)
                            ) {
                                Text("Bet")
                            }
                            Button(
                                onClick = { showWinnerDialog = true },
                                modifier = Modifier.width(buttonCellSize)
                            ) {
                                Text("Next")
                            }
                        } else {
                            // Fixed width spacers to preserve layout.
                            Spacer(modifier = Modifier.width(buttonCellSize))
                            Spacer(modifier = Modifier.width(buttonCellSize))
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display selected players and their scores/bet data.
        if (selectedPlayers.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                selectedPlayers.forEach { player ->
                    Text(
                        text = "${player.emoji} ${player.name}: ${player.score}\nScore: ${player.score}",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // --- Dialogs below ---

        // Set number of players dialog.
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

        // Player picker dialog.
        showPlayerPickerIndex?.let { index ->
            PlayerPickerDialog(
                allPlayers = playerViewModel.players.value,  // or allPlayers if you want to use local state.
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

        // Set number of rounds dialog.
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

        // Bet Amount Dialog with previous bet values loaded.
        if (showBetAmountDialog) {
            BetAmountDialog(
                players = selectedPlayers,
                initialBets = betAmounts[currentBetRow] ?: emptyMap(),
                onSaveBets = { betValues ->
                    updateBetAmountsForRow(currentBetRow, betValues)
                    showBetAmountDialog = false
                    // Optionally, you can call a function here to automatically move to the next row.
                },
                onDismiss = { showBetAmountDialog = false }
            )
        }

        // Winner selection Dialog.
        if (showWinnerDialog) {
            WinnerSelectionDialog(
                players = selectedPlayers,
                onDismiss = { showWinnerDialog = false },
                onFinish = { winners ->
                    val currentBets = betAmounts[currentBetRow]
                    selectedPlayers = selectedPlayers.map { player ->
                        // Example scoring logic: add bet + bonus for winners, subtract for non-winners.
                        val betValue = currentBets?.get(player) ?: 0
                        val bonus = 10
                        val newScore = if (player in winners) {
                            player.score + betValue + bonus
                        } else {
                            player.score - (betValue + bonus)
                        }
                        player.copy(score = newScore)
                    }
                    showWinnerDialog = false
                    // Move to the next active row, if applicable.
                    proceedToNextRow()
                }
            )
        }
    }
}
