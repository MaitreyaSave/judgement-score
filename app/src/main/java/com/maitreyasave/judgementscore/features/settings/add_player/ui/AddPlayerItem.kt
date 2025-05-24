package com.maitreyasave.judgementscore.features.settings.add_player.ui

import androidx.compose.runtime.Composable
import com.maitreyasave.judgementscore.features.settings.ui.SettingsItem

@Composable
fun AddPlayerItem(onClick: () -> Unit) {
    SettingsItem(
        title = "Add Player",
        onClick = onClick,
        showCheckbox = false,
        isChecked = false,
        onCheckedChange = {}
    )
}