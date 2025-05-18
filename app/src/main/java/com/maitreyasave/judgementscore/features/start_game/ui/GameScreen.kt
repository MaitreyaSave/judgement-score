package com.maitreyasave.judgementscore.features.start_game.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
    var numberOfPlayers by remember { mutableIntStateOf(0) }
    var selectedPlayers by remember { mutableStateOf<List<Player>>(emptyList()) }

    var showNumberDialog by remember { mutableStateOf(false) }
    var showPlayerPickerIndex by remember { mutableStateOf<Int?>(null) }
    var showRoundsDialog by remember { mutableStateOf(false) }
    var showBetAmountDialog by remember { mutableStateOf(false) }
    var showWinnerDialog by remember { mutableStateOf(false) }

    var gameStarted by remember { mutableStateOf(false) }
    var numberOfRounds by remember { mutableIntStateOf(0) }
    var buttonRowIndex by remember { mutableIntStateOf(0) }
    var currentBetRow by remember { mutableIntStateOf(0) }

    var betAmounts by remember { mutableStateOf<Map<Int, Map<String, Int>>>(emptyMap()) }

    val scrollState = rememberScrollState()

    fun updateBetAmounts(rowIndex: Int, betValues: Map<String, Int>) {
        betAmounts = betAmounts.toMutableMap().apply { put(rowIndex, betValues) }
    }

    fun proceedToNextPlayer(index: Int) {
        showPlayerPickerIndex = if (index + 1 < numberOfPlayers) index + 1 else null
    }

    fun proceedToNextRound() {
        currentBetRow++
        buttonRowIndex++
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(24.dp)
    ) {
        TopGameButtons(
            enabled = !gameStarted,
            canStart = selectedPlayers.size == numberOfPlayers && numberOfPlayers > 0,
            onSetPlayers = { showNumberDialog = true },
            onStartGame = {
                gameStarted = true
                showRoundsDialog = true
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (gameStarted && numberOfRounds > 0) {
            GameGrid(
                numberOfRounds = numberOfRounds,
                selectedPlayers = selectedPlayers,
                betAmounts = betAmounts,
                buttonRowIndex = buttonRowIndex,
                onBetClick = { showBetAmountDialog = true },
                onNextClick = { showWinnerDialog = true }
            )
        }

        if (selectedPlayers.isNotEmpty()) {
            PlayerScoresRow(selectedPlayers)
        }
    }

    if (showNumberDialog) {
        SetNumberOfPlayersDialog(
            onDismiss = { showNumberDialog = false },
            onConfirm = { count ->
                numberOfPlayers = count
                selectedPlayers = List(count) { Player("", "") }
                showPlayerPickerIndex = 0
                showNumberDialog = false
            }
        )
    }

    showPlayerPickerIndex?.let { index ->
        PlayerPickerDialog(
            allPlayers = playerViewModel.players.value,
            selectedPlayers = selectedPlayers,
            onAddNew = { name, emoji ->
                val newPlayer = Player(name, emoji)
                playerViewModel.addPlayer(name, emoji)
                selectedPlayers = selectedPlayers.toMutableList().apply { this[index] = newPlayer }
                proceedToNextPlayer(index)
            },
            onSelect = { player ->
                selectedPlayers = selectedPlayers.toMutableList().apply { this[index] = player }
                proceedToNextPlayer(index)
            },
            onDismiss = { showPlayerPickerIndex = null }
        )
    }

    if (showRoundsDialog) {
        RoundsDialog(
            onDismiss = { showRoundsDialog = false },
            onConfirm = {
                numberOfRounds = it
                showRoundsDialog = false
            }
        )
    }

    if (showBetAmountDialog) {
        BetAmountDialog(
            players = selectedPlayers,
            initialBets = betAmounts[currentBetRow] ?: emptyMap(),
            onSaveBets = {
                updateBetAmounts(currentBetRow, it)
                showBetAmountDialog = false
            },
            onDismiss = { showBetAmountDialog = false }
        )
    }

    if (showWinnerDialog) {
        WinnerSelectionDialog(
            players = selectedPlayers,
            onDismiss = { showWinnerDialog = false },
            onFinish = { winners ->
                val currentBets = betAmounts[currentBetRow]
                selectedPlayers = selectedPlayers.map { player ->
                    val bet = currentBets?.get(player.name) ?: 0
                    val bonus = 10
                    val newScore = if (player in winners) player.score + bet + bonus
                    else player.score - (bet + bonus)
                    player.copy(score = newScore)
                }
                showWinnerDialog = false
                proceedToNextRound()
            }
        )
    }
}
