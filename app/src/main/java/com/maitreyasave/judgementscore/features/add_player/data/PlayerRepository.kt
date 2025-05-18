package com.maitreyasave.judgementscore.features.add_player.data

import android.content.Context
import androidx.room.Room
import com.maitreyasave.judgementscore.database.PlayerDatabase

class PlayerRepository(context: Context) {

    private val db = Room.databaseBuilder(
        context.applicationContext,
        PlayerDatabase::class.java,
        "player_db"
    ).build()

    private val playerDao = db.playerDao()

    suspend fun loadPlayers(): List<Player> {
        return playerDao.getAllPlayers().map {
            Player(id = it.id, name = it.name, emoji = it.emoji)
        }
    }

    suspend fun addPlayer(player: Player) {
        val entity = PlayerEntity(name = player.name, emoji = player.emoji)
        playerDao.insertPlayer(entity)
    }

    suspend fun savePlayers(players: List<Player>) {
        val entities = players.map { PlayerEntity(name = it.name, emoji = it.emoji) }
        playerDao.insertPlayers(entities)
    }
}

