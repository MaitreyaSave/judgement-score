package com.maitreyasave.judgementscore.features.home.start_game.ui

import android.app.Activity
import android.app.Application
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.maitreyasave.judgementscore.di.MyApp
import com.maitreyasave.judgementscore.features.home.add_bet.BetAmountDialog
import com.maitreyasave.judgementscore.features.home.select_winner.WinnerSelectionDialog
import com.maitreyasave.judgementscore.features.home.start_game.CalculateRoundsViewModel
import com.maitreyasave.judgementscore.features.home.start_game.GameStateViewModel
import com.maitreyasave.judgementscore.features.home.start_game.data.GameState
import com.maitreyasave.judgementscore.features.settings.add_player.PlayerViewModel
import com.maitreyasave.judgementscore.features.settings.add_player.data.Player

@Composable
fun GameScreen() {
    val context = LocalContext.current
    val resumeGame = (context as? Activity)?.intent?.getBooleanExtra("resume", false) ?: false

    val appComponent = (context.applicationContext as MyApp).appComponent
    val playerFactory = remember { appComponent.playerViewModelFactory() }
    val playerViewModel: PlayerViewModel = viewModel(factory = playerFactory)
    val gameStateViewModel = remember {
        GameStateViewModel.getInstance(context.applicationContext as Application)
    }
    val calculateRoundsViewModel: CalculateRoundsViewModel = viewModel()
    val autoCalculate = calculateRoundsViewModel.shouldCalculateRounds()

    var numberOfPlayers by remember { mutableIntStateOf(0) }
    var selectedPlayers by remember { mutableStateOf<List<Player>>(emptyList()) }

    var showNumberDialog by remember { mutableStateOf(false) }
    var showPlayerPickerIndex by remember { mutableStateOf<Int?>(null) }
    var showRoundsDialog by remember { mutableStateOf(false) }
    var showBetAmountDialog by remember { mutableStateOf(false) }
    var showWinnerDialog by remember { mutableStateOf(false) }

    var gameStarted by remember { mutableStateOf(resumeGame) }
    var numberOfRounds by remember { mutableIntStateOf(0) }
    var buttonRowIndex by remember { mutableIntStateOf(0) }
    var currentBetRow by remember { mutableIntStateOf(0) }
    var maxHands by remember { mutableStateOf(0) }
    var betAmounts by remember { mutableStateOf<Map<Int, Map<String, Int>>>(emptyMap()) }

    val scrollState = rememberScrollState()

    // Load saved game state if needed
    LaunchedEffect(Unit) {
        if (resumeGame) {
            gameStateViewModel.loadGameState()?.let { savedState ->
                numberOfPlayers = savedState.numberOfPlayers
                selectedPlayers = savedState.selectedPlayers
                numberOfRounds = savedState.numberOfRounds
                buttonRowIndex = savedState.buttonRowIndex
                currentBetRow = savedState.currentBetRow
                betAmounts = savedState.betAmounts
                gameStarted = true
                maxHands = savedState.maxHands
            }
        }
    }

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

    // Save game state on changes
    LaunchedEffect(
        gameStarted,
        numberOfPlayers,
        selectedPlayers,
        numberOfRounds,
        buttonRowIndex,
        currentBetRow,
        betAmounts,
        maxHands
    ) {
        if (gameStarted) {
            val state = GameState(
                numberOfPlayers = numberOfPlayers,
                selectedPlayers = selectedPlayers,
                numberOfRounds = numberOfRounds,
                buttonRowIndex = buttonRowIndex,
                currentBetRow = currentBetRow,
                betAmounts = betAmounts,
                maxHands = maxHands
            )
            gameStateViewModel.saveGameState(state)
        }
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
                gameStateViewModel.markGameStarted()
                showRoundsDialog = !autoCalculate
            },
            onEndGame = {
                gameStarted = false
                gameStateViewModel.markGameEnded(selectedPlayers)
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
                onNextClick = { showWinnerDialog = true },
                numberOfCards = maxHands
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
                selectedPlayers = List(count) {
                    Player(id = 0, name = "", emoji = "")
                }
                showPlayerPickerIndex = 0
                showNumberDialog = false

                if(autoCalculate) {
                    calculateRoundsViewModel.calculateRoundsAndHands(numberOfPlayers)
                    numberOfRounds = calculateRoundsViewModel.getNumRounds()
                    maxHands = calculateRoundsViewModel.getMaxHands()
                }
            }
        )
    }

    showPlayerPickerIndex?.let { index ->
        PlayerPickerDialog(
            allPlayers = playerViewModel.players.value,
            selectedPlayers = selectedPlayers,
            onAddNew = { name, emoji ->
                val newPlayer = Player(name = name, emoji = emoji)
                playerViewModel.addPlayer(name, emoji)
                selectedPlayers = selectedPlayers.toMutableList().apply {
                    this[index] = newPlayer
                }
                proceedToNextPlayer(index)
            },
            onSelect = { player ->
                selectedPlayers = selectedPlayers.toMutableList().apply {
                    this[index] = player
                }
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
                calculateRoundsViewModel.calculateMaxHands(numberOfPlayers)
                maxHands = calculateRoundsViewModel.getMaxHands()
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

    // Handle end-of-game scenario
    LaunchedEffect(gameStarted, numberOfRounds, buttonRowIndex) {
        if (gameStarted && buttonRowIndex >= numberOfRounds) {
            gameStateViewModel.markGameEnded(selectedPlayers)
        }
    }
}
