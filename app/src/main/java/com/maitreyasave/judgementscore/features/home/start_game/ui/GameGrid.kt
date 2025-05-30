package com.maitreyasave.judgementscore.features.home.start_game.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.maitreyasave.judgementscore.features.settings.add_player.data.Player
import kotlinx.coroutines.delay

@Composable
fun GameGrid(
    numberOfRounds: Int,
    selectedPlayers: List<Player>,
    betAmounts: Map<Int, Map<String, Int>>,
    buttonRowIndex: Int,
    onUndoClick: () -> Unit,
    onBetClick: () -> Unit,
    onNextClick: () -> Unit,
    numberOfCards: Int,
    isPortrait: Boolean
) {
    val suits = listOf("♠️", "♥️", "♣️", "♦️", "⬜")
    val cellWidth = 60.dp
    val cellHeight = 30.dp
    val cardsPerRoundList = mutableListOf<Int>()

    // Precompute number of cards per round
    run {
        var cards = numberOfCards
        var decreasing = true
        repeat(numberOfRounds) {
            cardsPerRoundList.add(cards)
            if (decreasing) {
                cards--
                if (cards == 1) decreasing = false
            } else {
                cards++
            }
        }
    }

    val highlightColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)

    Column(modifier = Modifier.padding(16.dp)) {
        if (isPortrait) {
            val scrollState = rememberLazyListState()

            // Auto-scroll to the active column smoothly
            LaunchedEffect(buttonRowIndex) {
                // Delay ensures layout is composed before scroll
                delay(200)
                scrollState.animateScrollToItem(buttonRowIndex, scrollOffset = -180) // tweak offset as needed
            }

            // Snap behavior (scroll stops at nearest column)
            val snappingBehavior = rememberSnapFlingBehavior(lazyListState = scrollState)

            Row(modifier = Modifier.fillMaxWidth()) {
                // Fixed player name column
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.width(100.dp)
                ) {
                    Spacer(modifier = Modifier.height(28.dp))
                    selectedPlayers.forEach { player ->
                        Text(
                            text = "${player.emoji} ${player.name}",
                            modifier = Modifier.fillMaxWidth().height(cellHeight),
                            maxLines = 1,
                            textAlign = TextAlign.Start
                        )
                    }
                }

                // Scrollable suit headers and bets
                LazyRow(
                    state = scrollState,
                    flingBehavior = snappingBehavior,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .weight(1f)
                ) {
                    items(numberOfRounds) { i ->
                        val isHighlighted = i == buttonRowIndex
                        val borderModifier = if (isHighlighted) {
                            Modifier
                                .border(
                                    width = 2.dp,
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(4.dp)
                        } else {
                            Modifier
                        }

                        Column(
                            modifier = borderModifier,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            val suit = suits[i % suits.size]
                            val label = "$suit (${cardsPerRoundList[i]})"

                            Text(
                                text = label,
                                modifier = Modifier.width(cellWidth)
                                    .padding(0.dp, 3.dp),
                                textAlign = TextAlign.Center,
                                maxLines = 1
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            selectedPlayers.forEach { player ->
                                Text(
                                    text = "${betAmounts[i]?.get(player.name) ?: 0}",
                                    modifier = Modifier.width(cellWidth)
                                        .padding(0.dp, 2.dp)
                                        .height(cellHeight),
                                    textAlign = TextAlign.Center,
                                    maxLines = 1
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                            }
                        }
                    }
                }
            }

            // Bet & Next buttons (outside grid, always visible)
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = onUndoClick, modifier = Modifier.weight(1f)) {
                    Text("Undo")
                }
                Button(onClick = onBetClick, modifier = Modifier.weight(1f)) {
                    Text("Bet")
                }
                Button(onClick = onNextClick, modifier = Modifier.weight(1f)) {
                    Text("Next")
                }
            }
        } else {
            // Landscape mode
            Column {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Suit", Modifier.width(cellWidth), textAlign = TextAlign.Center)
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
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(if (i == buttonRowIndex) highlightColor else Color.Transparent)
                    ) {
                        Text(
                            "${suits[i % suits.size]} ($cards)",
                            Modifier.width(cellWidth).padding(start = 16.dp),
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
                            Button(onClick = onUndoClick, modifier = Modifier.weight(1f)) { Text("Undo") }
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
