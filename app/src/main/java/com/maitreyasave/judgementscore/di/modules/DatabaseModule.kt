package com.maitreyasave.judgementscore.di.modules

import android.content.Context
import androidx.room.Room
import com.maitreyasave.judgementscore.database.PlayerDatabase
import com.maitreyasave.judgementscore.features.add_player.data.PlayerDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun providePlayerDatabase(context: Context): PlayerDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            PlayerDatabase::class.java,
            "player_db"
        ).build()
    }

    @Provides
    @Singleton
    fun providePlayerDao(database: PlayerDatabase): PlayerDao {
        return database.playerDao()
    }
}
