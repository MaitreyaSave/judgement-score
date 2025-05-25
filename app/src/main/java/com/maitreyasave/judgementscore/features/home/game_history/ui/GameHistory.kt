package com.maitreyasave.judgementscore.features.home.game_history.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.maitreyasave.judgementscore.features.home.game_history.data.GameHistoryEntity
import java.text.DateFormat
import java.util.Date

@Composable
fun GameHistory(
    gameHistory: List<GameHistoryEntity>,
    onDeleteItem: (Int) -> Unit
) {
    var selectedScore by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Game History",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(gameHistory, key = { it.id }) { entry ->
                val summary = "Winner: ${entry.winnerName}"
                val scoreDetails = "Game played at ${
                    DateFormat.getDateTimeInstance().format(Date(entry.timestamp))
                }"

                GameHistoryCard(
                    id = entry.id.toString(),
                    summary = summary,
                    scoreDetails = scoreDetails,
                    onViewScore = { selectedScore = it },
                    onDeleteConfirmed = { onDeleteItem(entry.id) }
                )
            }
        }

        ScoreDialog(
            score = selectedScore,
            onClose = { selectedScore = null }
        )
    }
}
