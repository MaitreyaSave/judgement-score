package com.maitreyasave.judgementscore.features.home.start_game.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.maitreyasave.judgementscore.features.settings.add_player.data.Player
import com.maitreyasave.judgementscore.features.settings.ui.AddPlayerDialog

@Composable
fun PlayerPickerDialog(
    allPlayers: List<Player>,
    selectedPlayers: List<Player>,
    onAddNew: (String, String) -> Unit,
    onSelect: (Player) -> Unit,
    onDismiss: () -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }

    val filteredPlayers = allPlayers.filterNot { it in selectedPlayers }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Player") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                LazyColumn(
                    modifier = Modifier
                        .heightIn(max = 300.dp)
                        .fillMaxWidth()
                ) {
                    items(filteredPlayers) { player ->
                        TextButton(
                            onClick = { onSelect(player) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)
                        ) {
                            Text(
                                text = "${player.emoji} ${player.name}",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Start
                            )
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedButton(
                            onClick = { showAddDialog = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)
                        ) {
                            Text("Add New Player")
                        }
                    }
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

