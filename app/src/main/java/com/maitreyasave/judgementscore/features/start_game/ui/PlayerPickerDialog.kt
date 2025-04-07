package com.maitreyasave.judgementscore.features.start_game.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedButton
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
import com.maitreyasave.judgementscore.ui.AddPlayerDialog

@Composable
fun PlayerPickerDialog(
    allPlayers: List<Player>,
    onAddNew: (String, String) -> Unit,
    onSelect: (Player) -> Unit,
    onDismiss: () -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Player") },
        text = {
            Column {
                allPlayers.forEach { player ->
                    TextButton(onClick = { onSelect(player) }) {
                        Text("${player.emoji} ${player.name}")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(onClick = { showAddDialog = true }) {
                    Text("Add New Player")
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )

    if (showAddDialog) {
        AddPlayerDialog(
            onDismiss = { showAddDialog = false },
            onSave = { name, emoji ->
                onAddNew(name, emoji)
                showAddDialog = false
            }
        )
    }
}
