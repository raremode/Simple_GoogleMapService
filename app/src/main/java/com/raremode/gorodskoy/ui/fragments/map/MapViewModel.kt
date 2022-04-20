package com.raremode.gorodskoy.ui.fragments.map

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.raremode.gorodskoy.dao.marker.MarkerRepository
import com.raremode.gorodskoy.database.MarkerDatabase
import com.raremode.gorodskoy.models.MarkerLocation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MapViewModel(application: Application) : AndroidViewModel(application) {

    private val _markers = MutableSharedFlow<List<MarkerLocation>>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val markers: SharedFlow<List<MarkerLocation>> = _markers.asSharedFlow()

    private val repository: MarkerRepository = MarkerRepository(
        markerDao = MarkerDatabase.getDatabase(application).markerDao()
    )

    init {
        getAllMarkers()
    }

    fun addMarkers(markerLocations: List<MarkerLocation>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addMarkers(markerLocations = markerLocations)
        }
    }

    fun addMarker(markerLocation: MarkerLocation) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addMarker(markerLocation = markerLocation)
        }
    }

    fun getAllMarkers() {
        viewModelScope.launch(Dispatchers.IO) {
            _markers.emit(repository.readAllMarkersFromDb())
        }
    }

}