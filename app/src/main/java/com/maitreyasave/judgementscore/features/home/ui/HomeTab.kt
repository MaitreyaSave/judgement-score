package com.maitreyasave.judgementscore.features.home.ui

import android.app.Application
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.maitreyasave.judgementscore.features.home.game_history.ui.GameHistory
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

            GameHistory(gameHistory = gameHistory)
        }
    }
}
