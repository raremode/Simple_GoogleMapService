package com.raremode.gorodskoy.utils

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.raremode.gorodskoy.models.Marker


class MarkersHandler(
    private val googleMap: GoogleMap
) {

    fun setGarbageMarkers(markers: List<Marker>?) {
        markers?.forEach { marker ->
            googleMap.addMarker(
                MarkerOptions()
                    .position(
                        LatLng(
                            marker.coordinates.latitude?.toDoubleOrNull() ?: 0.0,
                            marker.coordinates.longitude?.toDoubleOrNull() ?: 0.0
                        )
                    )
            )
        }
    }
}