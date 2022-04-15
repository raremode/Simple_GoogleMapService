package com.raremode.gorodskoy.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.raremode.gorodskoy.models.Coordinates

class MarkerTypeConverter {

    @TypeConverter
    fun fromCoordinates(coordinates: Coordinates): String {
        return Gson().toJson(coordinates)
    }

    @TypeConverter
    fun toCoordinates(json: String): Coordinates {
        return Gson().fromJson(json, Coordinates::class.java)
    }
}