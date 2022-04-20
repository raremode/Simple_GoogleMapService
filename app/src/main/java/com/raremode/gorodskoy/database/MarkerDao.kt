package com.raremode.gorodskoy.database

import androidx.room.*
import com.raremode.gorodskoy.models.MarkerLocation

@Dao
interface MarkerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMarker(markerLocation: MarkerLocation)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMarkers(markerLocations: List<MarkerLocation>)

    @Delete
    suspend fun deleteMarker(markerLocation: MarkerLocation)

    @Query("DELETE FROM marker_table")
    suspend fun deleteAllMarkers()

    @Query("SELECT * FROM marker_table ORDER BY id ASC")
    suspend fun getAllMarkers(): List<MarkerLocation>
}