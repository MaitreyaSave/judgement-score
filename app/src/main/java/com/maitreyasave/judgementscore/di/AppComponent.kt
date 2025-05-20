package com.maitreyasave.judgementscore.di

import com.maitreyasave.judgementscore.di.modules.AppModule
import com.maitreyasave.judgementscore.di.modules.DatabaseModule
import com.maitreyasave.judgementscore.features.settings.add_player.PlayerViewModelFactory
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DatabaseModule::class])
interface AppComponent {
    fun playerViewModelFactory(): PlayerViewModelFactory
}
