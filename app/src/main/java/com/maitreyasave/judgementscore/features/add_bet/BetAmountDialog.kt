package com.maitreyasave.judgementscore.features.add_bet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.maitreyasave.judgementscore.features.settings.add_player.data.Player

@Composable
fun BetAmountDialog(
    players: List<Player>,
    initialBets: Map<String, Int> = emptyMap(),
    onSaveBets: (Map<String, Int>) -> Unit,
    onDismiss: () -> Unit
) {
    val betValues = remember { mutableStateMapOf<String, String>() }

    // Initialize the state with previous values or empty
    players.forEach { player ->
        if (betValues[player.name] == null) {
            betValues[player.name] = initialBets[player.name]?.toString() ?: ""
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Enter Bet Amounts") },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                players.forEach { player ->
                    OutlinedTextField(
                        value = betValues[player.name] ?: "",
                        onValueChange = { betValues[player.name] = it },
                        label = { Text("${player.name} Bet") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val result = betValues.mapValues { (_, value) -> value.toIntOrNull() ?: 0 }
                    onSaveBets(result)
                }
            ) {
                Text("Save Bets")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
