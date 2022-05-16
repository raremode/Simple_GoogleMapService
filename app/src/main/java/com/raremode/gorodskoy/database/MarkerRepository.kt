package com.raremode.gorodskoy.database

import com.raremode.gorodskoy.models.MarkerLocation

class MarkerRepository(private val markerDao: MarkerDao) {

    suspend fun addMarker(markerLocation: MarkerLocation) {
        markerDao.addMarker(markerLocation = markerLocation)
    }

    suspend fun addMarkers(markerLocations: List<MarkerLocation>) {
        markerDao.addMarkers(markerLocations = markerLocations)
    }

    suspend fun deleteMarker(markerLocation: MarkerLocation) {
        markerDao.deleteMarker(markerLocation = markerLocation)
    }

    suspend fun deleteAllMarkers() {
        markerDao.deleteAllMarkers()
    }

    suspend fun readAllMarkersFromDb(): List<MarkerLocation> {
        return markerDao.getAllMarkers()
    }

//    suspend fun updateMarker(marker: Marker) {
//        markerDao.updateMarker(marker = marker)
//    }
//
//    suspend fun updateMarkers(markers: List<Marker>) {
//        markerDao.updatedMarkers(markers = markers)
//    }

}