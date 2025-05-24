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

@Composable
fun GameHistory(gameHistory: List<Pair<String, String>>) {
    var selectedScore by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Game History",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(gameHistory) { (summary, scoreDetails) ->
                GameHistoryCard(
                    summary = summary,
                    scoreDetails = scoreDetails,
                    onViewScore = { selectedScore = it }
                )
            }
        }

        ScoreDialog(
            score = selectedScore,
            onClose = { selectedScore = null }
        )
    }
}
