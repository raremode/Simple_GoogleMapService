package com.raremode.gorodskoy.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "marker_table")
data class MarkerLocation(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String? = null,
    val garbageType: String? = null,
    val coordinates: Coordinates
)