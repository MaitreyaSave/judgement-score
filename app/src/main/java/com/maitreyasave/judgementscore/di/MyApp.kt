package com.maitreyasave.judgementscore.di

import android.app.Application
import com.maitreyasave.judgementscore.di.modules.AppModule
import com.maitreyasave.judgementscore.di.modules.DatabaseModule

class MyApp : Application() {

    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .databaseModule(DatabaseModule())
            .build()
    }
}
