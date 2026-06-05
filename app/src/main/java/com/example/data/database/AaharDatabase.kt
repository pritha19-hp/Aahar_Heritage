package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavoriteSpot::class, ChatMessageEntity::class, AddaSessionEntity::class, SearchQueryEntity::class], version = 3, exportSchema = false)
abstract class AaharDatabase : RoomDatabase() {
    abstract fun aaharDao(): AaharDao

    companion object {
        @Volatile
        private var INSTANCE: AaharDatabase? = null

        fun getDatabase(context: Context): AaharDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AaharDatabase::class.java,
                    "aahar_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
