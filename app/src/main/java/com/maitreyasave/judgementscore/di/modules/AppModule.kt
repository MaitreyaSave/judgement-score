package com.maitreyasave.judgementscore.di.modules

import android.content.Context
import com.maitreyasave.judgementscore.features.settings.add_player.PlayerViewModelFactory
import com.maitreyasave.judgementscore.features.settings.add_player.data.PlayerDao
import com.maitreyasave.judgementscore.features.settings.add_player.data.PlayerRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val context: Context) {

    @Provides
    @Singleton
    fun provideContext(): Context = context.applicationContext

    @Provides
    @Singleton
    fun providePlayerRepository(playerDao: PlayerDao): PlayerRepository {
        return PlayerRepository(playerDao)
    }

    @Provides
    @Singleton
    fun providePlayerViewModelFactory(repository: PlayerRepository): PlayerViewModelFactory {
        return PlayerViewModelFactory(repository)
    }
}
