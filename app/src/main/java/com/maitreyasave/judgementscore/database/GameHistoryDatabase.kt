package com.maitreyasave.judgementscore.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.maitreyasave.judgementscore.features.home.game_history.data.GameHistoryDao
import com.maitreyasave.judgementscore.features.home.game_history.data.GameHistoryEntity
import com.maitreyasave.judgementscore.features.home.game_history.data.PlayerScoreEntity

@Database(entities = [GameHistoryEntity::class, PlayerScoreEntity::class], version = 2)
abstract class GameHistoryDatabase : RoomDatabase() {
    abstract fun gameHistoryDao(): GameHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: GameHistoryDatabase? = null

        fun getInstance(context: Context): GameHistoryDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    GameHistoryDatabase::class.java,
                    "game_history.db"
                ).fallbackToDestructiveMigration(false)
                    .build().also { INSTANCE = it }
            }
        }
    }
}
