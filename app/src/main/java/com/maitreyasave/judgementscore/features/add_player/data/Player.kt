package com.maitreyasave.judgementscore.features.add_player.data

data class Player(
    val name: String,
    val emoji: String,
    var score: Int = 0 // Mutable score field
)