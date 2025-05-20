package com.maitreyasave.judgementscore.di

import android.app.Application
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.maitreyasave.judgementscore.di.modules.AppModule
import com.maitreyasave.judgementscore.di.modules.DatabaseModule

class MyApp : Application(), ViewModelStoreOwner {

    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .databaseModule(DatabaseModule())
            .build()
    }

    override val viewModelStore: ViewModelStore
        get() = ViewModelStore()
}
