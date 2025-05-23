package com.maitreyasave.judgementscore.features.home.game_history.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_history")
data class GameHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val winnerName: String,
    val timestamp: Long
)
