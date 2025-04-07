package com.maitreyasave.judgementscore.features.add_player.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PlayerRepository(private val context: Context) {

    fun savePlayers(players: List<Player>) {
        val prefs = context.getSharedPreferences("player_prefs", Context.MODE_PRIVATE)
        val json = Gson().toJson(players)
        prefs.edit().putString("key_player_list", json).apply()
    }

    fun loadPlayers(): List<Player> {
        val prefs = context.getSharedPreferences("player_prefs", Context.MODE_PRIVATE)
        val json = prefs.getString("key_player_list", null)
        return if (json != null) {
            val type = object : TypeToken<List<Player>>() {}.type
            Gson().fromJson(json, type)
        } else emptyList()
    }
}
