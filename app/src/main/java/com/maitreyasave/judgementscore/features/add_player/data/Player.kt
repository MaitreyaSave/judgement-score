package com.maitreyasave.judgementscore.features.add_player.data

import kotlinx.serialization.Serializable

@Serializable
data class Player(
    val id: Int = 0,
    val name: String = "",
    val emoji: String = "",
    var score: Int = 0 // Mutable score field
)