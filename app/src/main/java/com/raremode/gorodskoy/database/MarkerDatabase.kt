package com.raremode.gorodskoy.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.raremode.gorodskoy.models.MarkerLocation

@Database(entities = [MarkerLocation::class], version = 1, exportSchema = false)
@TypeConverters(MarkerTypeConverter::class)
abstract class MarkerDatabase : RoomDatabase() {

    abstract fun markerDao(): MarkerDao

    companion object {

        @Volatile
        private var INSTANCE: MarkerDatabase? = null

        fun getDatabase(context: Context): MarkerDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MarkerDatabase::class.java,
                    "marker_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}