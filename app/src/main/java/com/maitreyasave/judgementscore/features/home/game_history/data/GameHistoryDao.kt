package com.maitreyasave.judgementscore.features.home.game_history.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface GameHistoryDao {
    @Insert
    suspend fun insertGameHistory(history: GameHistoryEntity): Long

    @Insert
    suspend fun insertPlayerScores(scores: List<PlayerScoreEntity>)

    @Transaction
    suspend fun insertFullGame(history: GameHistoryEntity, scores: List<PlayerScoreEntity>) {
        val gameId = insertGameHistory(history)
        val scoresWithGameId = scores.map { it.copy(gameId = gameId.toInt()) }
        insertPlayerScores(scoresWithGameId)
    }

    @Query("SELECT * FROM game_history ORDER BY timestamp DESC")
    suspend fun getAllGameHistories(): List<GameHistoryEntity>

    @Query("SELECT * FROM PlayerScoreEntity WHERE gameId = :gameId")
    suspend fun getScoresForGame(gameId: Int): List<PlayerScoreEntity>

    @Query("SELECT * FROM game_history ORDER BY timestamp DESC")
    fun getAllGameHistoriesFlow(): Flow<List<GameHistoryEntity>>

}
