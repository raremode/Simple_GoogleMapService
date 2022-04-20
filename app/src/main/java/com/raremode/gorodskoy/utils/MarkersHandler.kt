package com.raremode.gorodskoy.utils

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.raremode.gorodskoy.models.GarbageTypes
import com.raremode.gorodskoy.models.MarkerLocation


class MarkersHandler(
    private val googleMap: GoogleMap
) {

    fun setGarbageMarkers(markerLocations: List<MarkerLocation>?) {
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
                GarbageTypes.BATTERIES.type -> BitmapDescriptorFactory.HUE_ORANGE
                GarbageTypes.PLASTIC.type -> BitmapDescriptorFactory.HUE_GREEN
                else -> { BitmapDescriptorFactory.HUE_MAGENTA }
            }
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(iconColor))
            googleMap.addMarker(markerOptions)
        }
    }
}