package com.maitreyasave.judgementscore.features.home.ui

import android.app.Application
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.maitreyasave.judgementscore.features.home.start_game.GameActivity
import com.maitreyasave.judgementscore.features.home.start_game.GameStateViewModel

@Composable
fun HomeTab() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val viewModel = remember {
        GameStateViewModel.getInstance(context.applicationContext as Application)
    }

    val hasSavedGame by viewModel.hasSavedGame.collectAsState()
    val gameHistory by viewModel.gameHistory.collectAsState(initial = emptyList())
    var selectedScore by remember { mutableStateOf<String?>(null) }

    // Refresh game status on resume
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.refreshGameStatus()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = {
                val intent = Intent(context, GameActivity::class.java).apply {
                    putExtra("resume", false)
                }
                context.startActivity(intent)
            }) {
                Text("Start Game (Landscape)")
            }

            if (hasSavedGame) {
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    val intent = Intent(context, GameActivity::class.java).apply {
                        putExtra("resume", true)
                    }
                    context.startActivity(intent)
                }) {
                    Text("Continue Game")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Game History",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(gameHistory) { (summary, scoreDetails) ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(12.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = summary,
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(onClick = { selectedScore = scoreDetails + "\n" }) {
                                Text("View Score")
                            }
                        }
                    }
                }
            }
        }

        // Score Dialog
        if (selectedScore != null) {
            AlertDialog(
                onDismissRequest = { selectedScore = null },
                title = { Text("Full Score") },
                text = { Text(selectedScore ?: "") },
                confirmButton = {
                    TextButton(onClick = { selectedScore = null }) {
                        Text("Close")
                    }
                }
            )
        }
    }
}
