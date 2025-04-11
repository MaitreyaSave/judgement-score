package com.maitreyasave.judgementscore.features.add_bet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.maitreyasave.judgementscore.features.add_player.data.Player

@Composable
fun BetAmountDialog(
    players: List<Player>,
    onSaveBets: (Map<Player, Int>) -> Unit,
    onDismiss: () -> Unit
) {
    var betValues by remember { mutableStateOf<Map<Player, Int>>(emptyMap()) }

    val scrollState = rememberScrollState()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Enter Bet Amounts") },
        text = {
            // Make the Column scrollable
            Column(modifier = Modifier.verticalScroll(scrollState)) {
                players.forEach { player ->
                    var bet by remember { mutableStateOf("") }
                    OutlinedTextField(
                        value = bet,
                        onValueChange = { bet = it },
                        label = { Text("${player.name} Bet") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    betValues = betValues + (player to (bet.toIntOrNull() ?: 0))
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onSaveBets(betValues) }
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
