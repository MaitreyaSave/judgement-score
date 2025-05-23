package com.maitreyasave.judgementscore.features.settings.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.maitreyasave.judgementscore.di.MyApp
import com.maitreyasave.judgementscore.features.settings.add_player.PlayerViewModel
import com.maitreyasave.judgementscore.features.settings.calculate_rounds.ShouldCalculateRoundsViewModel

@Composable
fun AddPlayerDialog(
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var selectedEmoji by remember { mutableStateOf("😀") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isNotBlank()) {
                        onSave(name, selectedEmoji)
                    }
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = { Text("Add New Player") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Player Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("Choose an Emoji:")

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("😀", "😎", "🧠", "👽", "🐱", "🔥").forEach { emoji ->
                        Text(
                            text = emoji,
                            modifier = Modifier
                                .clickable { selectedEmoji = emoji }
                                .padding(4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text("Selected: $selectedEmoji")
            }
        }
    )
}

@Composable
fun ProfileTab() {
    val context = LocalContext.current
    val appComponent = (context.applicationContext as MyApp).appComponent

    val factory = remember { appComponent.playerViewModelFactory() }
    val playerViewModel: PlayerViewModel = viewModel(factory = factory)
    val shouldCalculateRoundsViewModel: ShouldCalculateRoundsViewModel = viewModel()

    val players by playerViewModel.players
    val isChecked by shouldCalculateRoundsViewModel.isFeatureEnabled.collectAsState()
    var showAddPlayerDialog by remember { mutableStateOf(false) }
//    var autoCalculateRounds by remember { mutableStateOf(true) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Settings", style = MaterialTheme.typography.headlineMedium)


        SettingsItem(
            title = "Auto-calculate rounds",
            onClick = {
                // Clicking the item should also update the checkbox
//                autoCalculateRounds = !autoCalculateRounds
                shouldCalculateRoundsViewModel.onFeatureEnabledChanged(!isChecked)
            },
            showCheckbox = true,
            isChecked = isChecked,
            onCheckedChange = { shouldCalculateRoundsViewModel.onFeatureEnabledChanged(it) }
        )


        SettingsItem(
            title = "Add Player",
            onClick = { showAddPlayerDialog = true },
            showCheckbox = false,
            isChecked = false,
            onCheckedChange = {
                // No-op
            }
        )

        Text("Players List:", style = MaterialTheme.typography.bodyLarge)
        players.forEach { player -> Text("${player.name} ${player.emoji}") }

        if (showAddPlayerDialog) {
            AddPlayerDialog(
                onDismiss = { showAddPlayerDialog = false },
                onSave = { name, emoji ->
                    playerViewModel.addPlayer(name, emoji)
                    showAddPlayerDialog = false
                }
            )
        }
    }
}


