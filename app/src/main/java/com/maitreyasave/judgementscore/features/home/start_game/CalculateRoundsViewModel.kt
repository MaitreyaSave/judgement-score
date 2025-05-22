package com.maitreyasave.judgementscore.features.home.start_game

import androidx.lifecycle.ViewModel
import com.maitreyasave.judgementscore.features.settings.shared.SettingsRepository

class CalculateRoundsViewModel: ViewModel() {
    private val isFeatureEnabled = SettingsRepository.shouldCalculateRoundsEnabled
    private var maxHands: Int = 0
    private var numRounds: Int = 0

    fun shouldCalculateRounds() = isFeatureEnabled.value
    fun getMaxHands() = maxHands
    fun getNumRounds() = numRounds


    fun calculateRoundsAndHands(numPlayers: Int) {
        if(shouldCalculateRounds()) {
            calculateMaxHands(numPlayers)
            numRounds = 2 * maxHands + 1
        }
    }

    fun calculateMaxHands(numPlayers: Int) {
        maxHands = TOTAL_NUM_CARDS / numPlayers
    }

    companion object {
        const val TOTAL_NUM_CARDS = 52
    }
}