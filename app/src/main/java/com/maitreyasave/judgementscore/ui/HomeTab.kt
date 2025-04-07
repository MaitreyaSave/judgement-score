package com.maitreyasave.judgementscore.ui

import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.maitreyasave.judgementscore.features.start_game.GameActivity

@Composable
fun HomeTab() {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = {
            val intent = Intent(context, GameActivity::class.java)
            context.startActivity(intent)
        }) {
            Text("Start Game (Landscape)")
        }
    }
}
