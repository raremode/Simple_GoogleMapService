package com.example.android.navigationadvancedsample.utils

import com.example.android.navigationadvancedsample.models.Marker
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MarkersHandler(
    private val googleMap: GoogleMap
) {

    fun setGarbageMarkers(markers: List<Marker>) {
        markers.forEach { marker ->
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