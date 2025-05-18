package com.maitreyasave.judgementscore.ui

import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.maitreyasave.judgementscore.features.start_game.GameActivity
import com.maitreyasave.judgementscore.features.start_game.GameStateViewModel

@Composable
fun HomeTab() {
    val context = LocalContext.current
    val viewModel: GameStateViewModel = viewModel() // no factory needed since AndroidViewModel is used
    val hasSavedGame by viewModel.hasSavedGame.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = {
                val intent = Intent(context, GameActivity::class.java).apply {
                    putExtra("resume", false) // Start a new game
                }
                context.startActivity(intent)
            }) {
                Text("Start Game (Landscape)")
            }

            if (hasSavedGame) {
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    val intent = Intent(context, GameActivity::class.java).apply {
                        putExtra("resume", true) // Resume saved game
                    }
                    context.startActivity(intent)
                }) {
                    Text("Continue Game")
                }
            }
        }
    }
}

