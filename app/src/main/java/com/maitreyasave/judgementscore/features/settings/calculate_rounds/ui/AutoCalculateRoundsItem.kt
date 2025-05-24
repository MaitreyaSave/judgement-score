package com.maitreyasave.judgementscore.features.settings.calculate_rounds.ui

import androidx.compose.runtime.Composable
import com.maitreyasave.judgementscore.features.settings.ui.SettingsItem

@Composable
fun AutoCalculateRoundsItem(isChecked: Boolean, onToggle: (Boolean) -> Unit) {
    SettingsItem(
        title = "Auto-calculate rounds",
        onClick = { onToggle(!isChecked) },
        showCheckbox = true,
        isChecked = isChecked,
        onCheckedChange = onToggle
    )
}