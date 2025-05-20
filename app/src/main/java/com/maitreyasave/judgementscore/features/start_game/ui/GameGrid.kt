package com.maitreyasave.judgementscore.features.start_game.ui

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
    onNextClick: () -> Unit
) {
    val suits = listOf("♠️", "♥️", "♣️", "♦️", "⬜")
    val suitCellSize = 40.dp
    val buttonCellSize = 100.dp

    Column(modifier = Modifier.padding(top = 24.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Suit", Modifier.width(suitCellSize), textAlign = TextAlign.Center)
            selectedPlayers.forEach {
                Text("${it.emoji} ${it.name}", Modifier.weight(1f), textAlign = TextAlign.Center, maxLines = 1)
            }
            Text("Bet", Modifier.width(buttonCellSize), textAlign = TextAlign.Center)
            Text("Next", Modifier.width(buttonCellSize), textAlign = TextAlign.Center)
        }

        Spacer(modifier = Modifier.height(8.dp))

        for (i in 0 until numberOfRounds) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    suits[i % suits.size],
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
                    Button(onClick = onBetClick, modifier = Modifier.width(buttonCellSize)) { Text("Bet") }
                    Button(onClick = onNextClick, modifier = Modifier.width(buttonCellSize)) { Text("Next") }
                } else {
                    Spacer(modifier = Modifier.width(buttonCellSize))
                    Spacer(modifier = Modifier.width(buttonCellSize))
                }
            }
        }
    }
}
