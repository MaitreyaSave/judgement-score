package com.maitreyasave.judgementscore.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.maitreyasave.judgementscore.features.settings.add_player.data.PlayerEntity

@Database(
    entities = [PlayerEntity::class],
    version = 1
)
abstract class GameDatabase : RoomDatabase() {
//    abstract fun playerDao(): PlayerDao
//    abstract fun scoreDao(): ScoreDao
//    abstract fun gameStateDao(): GameStateDao
}
