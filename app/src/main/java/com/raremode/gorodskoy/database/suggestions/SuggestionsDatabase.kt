package com.raremode.gorodskoy.database.suggestions

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SuggestionModel::class], version = 1, exportSchema = false)
abstract class SuggestionsDatabase : RoomDatabase() {

    abstract fun suggestionDao(): SuggestionsDao

    companion object {

        @Volatile
        private var INSTANCE: SuggestionsDatabase? = null

        fun getDatabase(context: Context): SuggestionsDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SuggestionsDatabase::class.java,
                    "suggestions_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}