package com.maitreyasave.judgementscore.features.add_player

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.maitreyasave.judgementscore.features.add_player.data.PlayerRepository

class PlayerViewModelFactory(context: Context) : ViewModelProvider.Factory {
    private val repository = PlayerRepository(context.applicationContext)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlayerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlayerViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

