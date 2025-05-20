package com.maitreyasave.judgementscore.features.home.select_winner

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.maitreyasave.judgementscore.features.settings.add_player.data.Player

@Composable
fun WinnerSelectionDialog(
    players: List<Player>,
    onDismiss: () -> Unit,
    onFinish: (Set<Player>) -> Unit
) {
    var selected by remember { mutableStateOf(emptySet<Player>()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Round Winners") },
        text = {
            LazyColumn(
                modifier = Modifier
                    .heightIn(max = 300.dp)
                    .padding(top = 8.dp) // Optional: spacing between title and list
            ) {
                items(players) { player ->
                    val isChecked = selected.contains(player)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp, horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${player.emoji} ${player.name}",
                            modifier = Modifier.weight(1f)
                                .padding(0.dp, 4.dp, 0.dp, 0.dp)
                        )
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