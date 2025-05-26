package com.maitreyasave.judgementscore.features.home.start_game.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.maitreyasave.judgementscore.features.settings.add_player.data.Player

@Composable
fun GameGrid(
    numberOfRounds: Int,
    selectedPlayers: List<Player>,
    betAmounts: Map<Int, Map<String, Int>>,
    buttonRowIndex: Int,
    onBetClick: () -> Unit,
    onNextClick: () -> Unit,
    numberOfCards: Int,
    isPortrait: Boolean
) {
    val suits = listOf("♠️", "♥️", "♣️", "♦️", "⬜")
    val suitCellSize = 60.dp
    var numCardsPerRound = numberOfCards
    var cardsPerRoundList = mutableListOf<Int>()

    // Precompute number of cards per round
    run {
        var cards = numberOfCards
        var decreasing = true
        repeat(numberOfRounds) {
            cardsPerRoundList.add(cards)
            if (decreasing) {
                cards--
                if (cards == 0) decreasing = false
            } else {
                cards++
            }
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        if (isPortrait) {
            // Portrait Mode: Transposed Grid (scrollable suits)
            Row(modifier = Modifier.fillMaxWidth()) {
                // Left fixed player column
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.width(100.dp)
                ) {
                    Spacer(modifier = Modifier.height(28.dp)) // top-left corner
                    selectedPlayers.forEach { player ->
                        Text(
                            text = "${player.emoji} ${player.name}",
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 1,
                            textAlign = TextAlign.Start
                        )
                    }
                }

                // Horizontally scrollable suit headers and bets
                Column(
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState())
                        .padding(start = 8.dp)
                ) {
                    // Suit header row
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        for (i in 0 until numberOfRounds) {
                            val suit = suits[i % suits.size]
                            val label = "$suit (${cardsPerRoundList[i]})"
                            Text(
                                text = label,
                                modifier = Modifier
                                    .width(suitCellSize)
                                    .align(Alignment.CenterVertically),
                                textAlign = TextAlign.Center,
                                maxLines = 1
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Bet rows for each player
                    selectedPlayers.forEach { player ->
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            for (i in 0 until numberOfRounds) {
                                Text(
                                    text = "${betAmounts[i]?.get(player.name) ?: 0}",
                                    modifier = Modifier
                                        .width(suitCellSize),
                                    textAlign = TextAlign.Center,
                                    maxLines = 1
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }

            // Buttons
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                Button(onClick = onBetClick, modifier = Modifier.weight(1f)) { Text("Bet") }
                Button(onClick = onNextClick, modifier = Modifier.weight(1f)) { Text("Next") }
            }

        } else {
            // Landscape: Existing grid layout
            Column {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Suit", Modifier.width(suitCellSize), textAlign = TextAlign.Center)
                    selectedPlayers.forEach {
                        Text("${it.emoji} ${it.name}", Modifier.weight(1f), textAlign = TextAlign.Start, maxLines = 1)
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Spacer(modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(8.dp))

                var cards = numberOfCards
                var decreasing = true

                for (i in 0 until numberOfRounds) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            "${suits[i % suits.size]} ($cards)",
                            Modifier.width(suitCellSize).padding(start = 16.dp),
                            textAlign = TextAlign.Center
                        )
                        selectedPlayers.forEach { player ->
                            Text(
                                "${betAmounts[i]?.get(player.name) ?: 0}",
                                Modifier.weight(1f),
                                textAlign = TextAlign.Center,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                        if (i == buttonRowIndex) {
                            Button(onClick = onBetClick, modifier = Modifier.weight(1f)) { Text("Bet") }
                            Button(onClick = onNextClick, modifier = Modifier.weight(1f)) { Text("Next") }
                        } else {
                            Spacer(modifier = Modifier.weight(1f))
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }

                    if (decreasing) {
                        cards--
                        if (cards == 0) decreasing = false
                    } else {
                        cards++
                    }
                }
            }
        }
    }
}
