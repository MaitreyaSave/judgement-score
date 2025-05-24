package com.maitreyasave.judgementscore.features.home.game_history.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun ScoreDialog(
    score: String?,
    onClose: () -> Unit
) {
    if (score != null) {
        AlertDialog(
            onDismissRequest = onClose,
            title = { Text("Full Score") },
            text = { Text(score) },
            confirmButton = {
                TextButton(onClick = onClose) {
                    Text("Close")
                }
            }
        )
    }
}
