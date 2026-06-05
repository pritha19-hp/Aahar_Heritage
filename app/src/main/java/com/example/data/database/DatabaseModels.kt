package com.example.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_spots")
data class FavoriteSpot(
    @PrimaryKey val id: String, // String ID of the spot to sync with local list
    val name: String,
    val description: String,
    val iconicFor: String,
    val location: String,
    val era: String,
    val imageUrl: String = "",
    val addedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sender: String, // "user" or "concierge"
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "adda_sessions")
data class AddaSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val moodName: String,
    val moodIcon: String,
    val companions: String,
    val fuelType: String,
    val recommendation: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "search_history")
data class SearchQueryEntity(
    @PrimaryKey val queryText: String,
    val timestamp: Long = System.currentTimeMillis()
)

