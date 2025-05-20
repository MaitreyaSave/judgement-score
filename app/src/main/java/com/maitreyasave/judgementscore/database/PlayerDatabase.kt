package com.maitreyasave.judgementscore.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.maitreyasave.judgementscore.features.settings.add_player.data.PlayerDao
import com.maitreyasave.judgementscore.features.settings.add_player.data.PlayerEntity

@Database(entities = [PlayerEntity::class], version = 1)
abstract class PlayerDatabase : RoomDatabase() {
    abstract fun playerDao(): PlayerDao
}
