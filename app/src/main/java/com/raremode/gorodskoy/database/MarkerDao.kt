package com.raremode.gorodskoy.database

import androidx.room.*
import com.raremode.gorodskoy.models.Marker

@Dao
interface MarkerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMarker(marker: Marker)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMarkers(markers: List<Marker>)

    @Delete
    suspend fun deleteMarker(marker: Marker)

    @Query("DELETE FROM marker_table")
    suspend fun deleteAllMarkers()

    @Query("SELECT * FROM marker_table ORDER BY id ASC")
    suspend fun getAllMarkers(): List<Marker>
}