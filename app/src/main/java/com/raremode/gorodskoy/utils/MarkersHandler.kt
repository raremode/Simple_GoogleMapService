package com.raremode.gorodskoy.utils

import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.raremode.gorodskoy.models.GarbageTypes
import com.raremode.gorodskoy.models.MarkerLocation


class MarkersHandler {

    fun getMarkerOptions(markerLocations: List<MarkerLocation>?): List<MarkerOptions> {
        val markersOptions = mutableListOf<MarkerOptions>()
        markerLocations?.forEach { marker ->
            val markerOptions = MarkerOptions()
                .position(
                    LatLng(
                        marker.coordinates.latitude?.toDoubleOrNull() ?: 0.0,
                        marker.coordinates.longitude?.toDoubleOrNull() ?: 0.0
                    )
                )
            val iconColor = when (marker.garbageType) {
                GarbageTypes.GLASS.type -> BitmapDescriptorFactory.HUE_RED
                GarbageTypes.BATTERIES.type -> BitmapDescriptorFactory.HUE_GREEN
                GarbageTypes.PLASTIC.type -> BitmapDescriptorFactory.HUE_YELLOW
                else -> { BitmapDescriptorFactory.HUE_MAGENTA }
            }
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(iconColor))
            markersOptions.add(markerOptions)
        }
        return markersOptions
    }
}