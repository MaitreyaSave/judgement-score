package com.maitreyasave.judgementscore.features.settings.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.maitreyasave.judgementscore.di.MyApp
import com.maitreyasave.judgementscore.features.settings.add_player.PlayerViewModel
import com.maitreyasave.judgementscore.features.settings.add_player.ui.AddPlayerDialog
import com.maitreyasave.judgementscore.features.settings.calculate_rounds.ShouldCalculateRoundsViewModel


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

    SettingsContent(
        players = players,
        isChecked = isChecked,
        onFeatureToggle = { shouldCalculateRoundsViewModel.onFeatureEnabledChanged(it) },
        onAddPlayerClick = { showAddPlayerDialog = true }
    )

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
