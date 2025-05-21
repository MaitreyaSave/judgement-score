package com.maitreyasave.judgementscore.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.maitreyasave.judgementscore.di.modules.AppModule
import com.maitreyasave.judgementscore.di.modules.DatabaseModule
import com.maitreyasave.judgementscore.features.settings.shared.SettingsRepository

val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")


class MyApp : Application(), ViewModelStoreOwner {

    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        SettingsRepository.init(this.settingsDataStore)
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .databaseModule(DatabaseModule())
            .build()
    }

    override val viewModelStore: ViewModelStore
        get() = ViewModelStore()
}
