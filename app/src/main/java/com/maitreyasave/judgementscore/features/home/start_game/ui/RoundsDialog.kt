package com.maitreyasave.judgementscore.features.home.start_game.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun RoundsDialog(onDismiss: () -> Unit, onConfirm: (Int) -> Unit) {
    var input by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Enter Number of Rounds") },
        text = {
            OutlinedTextField(
                value = input,
                onValueChange = { if (it.all(Char::isDigit)) input = it },
                label = { Text("Rounds") },
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirm(input.toIntOrNull() ?: 0)
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
