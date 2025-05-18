package com.maitreyasave.judgementscore.features.add_player.data

import javax.inject.Inject


class PlayerRepository @Inject constructor(
    private val playerDao: PlayerDao
) {

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


