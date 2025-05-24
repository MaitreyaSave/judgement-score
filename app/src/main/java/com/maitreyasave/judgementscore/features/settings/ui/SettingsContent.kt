package com.maitreyasave.judgementscore.features.settings.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.maitreyasave.judgementscore.features.settings.add_player.data.Player
import com.maitreyasave.judgementscore.features.settings.add_player.ui.AddPlayerItem
import com.maitreyasave.judgementscore.features.settings.calculate_rounds.ui.AutoCalculateRoundsItem

@Composable
fun SettingsContent(
    players: List<Player>,
    isChecked: Boolean,
    onFeatureToggle: (Boolean) -> Unit,
    onAddPlayerClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Settings", style = MaterialTheme.typography.headlineMedium)

        AutoCalculateRoundsItem(
            isChecked = isChecked,
            onToggle = onFeatureToggle
        )

        AddPlayerItem(onClick = onAddPlayerClick)

        PlayersList(players)
    }
}
