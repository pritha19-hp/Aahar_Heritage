package com.example.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AaharDao {
    // --- Favorite Spots ---
    @Query("SELECT * FROM favorite_spots ORDER BY addedAt DESC")
    fun getFavoriteSpots(): Flow<List<FavoriteSpot>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteSpot(spot: FavoriteSpot)

    @Delete
    suspend fun deleteFavoriteSpot(spot: FavoriteSpot)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_spots WHERE id = :spotId LIMIT 1)")
    suspend fun isFavorite(spotId: String): Boolean

    @Query("DELETE FROM favorite_spots WHERE id = :spotId")
    suspend fun deleteFavoriteSpotById(spotId: String)

    // --- Chat Messages ---
    @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC")
    fun getChatMessages(): Flow<List<ChatMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatMessage(message: ChatMessageEntity)

    @Query("DELETE FROM chat_messages")
    suspend fun clearChatHistory()

    // --- Adda Sessions ---
    @Query("SELECT * FROM adda_sessions ORDER BY timestamp DESC")
    fun getAddaSessions(): Flow<List<AddaSessionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddaSession(session: AddaSessionEntity)

    @Delete
    suspend fun deleteAddaSession(session: AddaSessionEntity)

    @Query("DELETE FROM adda_sessions")
    suspend fun clearAllAddaSessions()

    // --- Search History ---
    @Query("SELECT * FROM search_history ORDER BY timestamp DESC LIMIT 10")
    fun getRecentSearchQueries(): Flow<List<SearchQueryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearchQuery(searchQuery: SearchQueryEntity)

    @Query("DELETE FROM search_history WHERE queryText = :queryText")
    suspend fun deleteSearchQuery(queryText: String)

    @Query("DELETE FROM search_history")
    suspend fun clearSearchHistory()
}
