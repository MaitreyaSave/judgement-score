package com.maitreyasave.judgementscore.features.select_winner

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.maitreyasave.judgementscore.features.add_player.data.Player

@Composable
fun WinnerSelectionDialog(
    players: List<Player>,
    onDismiss: () -> Unit,
    onFinish: (Set<Player>) -> Unit
) {
    // Default to empty set so checkboxes are unchecked by default.
    var selected by remember { mutableStateOf(emptySet<Player>()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Round Winners") },
        text = {
            Column {
                players.forEach { player ->
                    val isChecked = selected.contains(player)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("${player.emoji} ${player.name}")
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = { checked ->
                                selected = if (checked) selected + player else selected - player
                            }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onFinish(selected)
                onDismiss()
            }) {
                Text("Finish")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
