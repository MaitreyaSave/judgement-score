package com.maitreyasave.judgementscore.features.home.start_game.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
    numberOfCards: Int
) {
    val suits = listOf("♠️", "♥️", "♣️", "♦️", "⬜")
    val suitCellSize = 80.dp
    var numCardsPerRound = numberOfCards
    var decreasing = true

    Column(modifier = Modifier.padding(top = 24.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Suit", Modifier.width(suitCellSize), textAlign = TextAlign.Center)
            selectedPlayers.forEach {
                Text("${it.emoji} ${it.name}", Modifier.weight(1f), textAlign = TextAlign.Start, maxLines = 1)
            }
            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(8.dp))

        for (i in 0 until numberOfRounds) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    suits[i % suits.size] + " ($numCardsPerRound)",
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

            if(decreasing) {
                numCardsPerRound--
                if(numCardsPerRound == 0) {
                    decreasing = false
                }
            } else {
                numCardsPerRound++
            }
        }
    }
}


