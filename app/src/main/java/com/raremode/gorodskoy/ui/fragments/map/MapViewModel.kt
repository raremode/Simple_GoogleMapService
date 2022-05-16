package com.raremode.gorodskoy.ui.fragments.map

import android.annotation.SuppressLint
import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.MarkerOptions
import com.raremode.gorodskoy.database.MarkerRepository
import com.raremode.gorodskoy.database.MarkerDatabase
import com.raremode.gorodskoy.database.suggestions.SuggestionModel
import com.raremode.gorodskoy.database.suggestions.SuggestionsDatabase
import com.raremode.gorodskoy.database.suggestions.SuggestionsRepository
import com.raremode.gorodskoy.models.GarbageTypes
import com.raremode.gorodskoy.models.MarkerLocation
import com.raremode.gorodskoy.ui.models.FilterButtonModel
import com.raremode.gorodskoy.utils.JsonAssetsManager
import com.raremode.gorodskoy.utils.MarkersHandler
import kotlinx.coroutines.*

class MapViewModel(application: Application) : AndroidViewModel(application) {

    private val _markers = MutableLiveData<List<MarkerOptions>>()
    val markers: LiveData<List<MarkerOptions>> = _markers

    private val repository: MarkerRepository = MarkerRepository(
        markerDao = MarkerDatabase.getDatabase(application).markerDao()
    )

    private val suggestionRepo: SuggestionsRepository = SuggestionsRepository(
        suggestionsDao = SuggestionsDatabase.getDatabase(application).suggestionDao()
    )

    private val _location = MutableLiveData<Location>()
    val location: LiveData<Location> = _location

    private val _filterButtons = MutableLiveData<List<FilterButtonModel>>()
    val filterButtons: LiveData<List<FilterButtonModel>> = _filterButtons

    private val _suggestions = MutableLiveData<List<String>>()
    val suggestions: LiveData<List<String>> = _suggestions

    private var markersLocation = listOf<MarkerLocation>()
    private var markersHandler: MarkersHandler
    private var updateLocationJob: Job? = null
    private var jsonAssetsManager = JsonAssetsManager(application)

    init {
        getAllMarkers()
        initFilterButtons()
        markersHandler = MarkersHandler()
        jsonAssetsManager.parseJsonFromAssets {
            addMarkers(it)
        }
    }

    private fun initFilterButtons() {
        val filterButtonItems = mutableListOf<FilterButtonModel>()
        filterButtonItems.add(FilterButtonModel("Всё", GarbageTypes.All, true))
        filterButtonItems.add(FilterButtonModel("Пластик", GarbageTypes.PLASTIC, false))
        filterButtonItems.add(FilterButtonModel("Батарейки", GarbageTypes.BATTERIES, false))
        filterButtonItems.add(FilterButtonModel("Стекло", GarbageTypes.GLASS, false))
        _filterButtons.value = filterButtonItems
    }

    fun setGarbageMarkers(type: GarbageTypes) {
        var markers = markersLocation.toList()
        markers = when (type) {
            GarbageTypes.BATTERIES -> markers.filter { it.garbageType == GarbageTypes.BATTERIES.type }
            GarbageTypes.GLASS -> markers.filter { it.garbageType == GarbageTypes.GLASS.type }
            GarbageTypes.PLASTIC -> markers.filter { it.garbageType == GarbageTypes.PLASTIC.type }
            else -> markers
        }
        _markers.value = markersHandler.getMarkerOptions(markers)
    }

    fun updateFilterButtonsState(position: Int) {
        val list = filterButtons.value ?: emptyList()
        list.forEachIndexed { index, button ->
            button.isSelected = index == position
        }
        _filterButtons.value = list
    }

    @SuppressLint("MissingPermission")
    fun runUpdatingLocation(fusedLocationProviderClient: FusedLocationProviderClient) {
        updateLocationJob?.cancel()
        updateLocationJob = viewModelScope.launch {
            while (isActive) {
                runCatching {
                    fusedLocationProviderClient.lastLocation
                }.onSuccess { location ->
                    location.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            _location.value = task.result
                        }
                    }
                }
                delay(2000)
            }
        }
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

    private fun getAllMarkers() {
        viewModelScope.launch(Dispatchers.IO) {
            val markersFromDB = repository.readAllMarkersFromDb()
            _markers.postValue(markersHandler.getMarkerOptions(markersFromDB))
            markersLocation = markersFromDB
        }
    }

    fun getAllSuggestions() {
        viewModelScope.launch(Dispatchers.IO) {
        _suggestions.postValue(suggestionRepo.getAllItems().map { it.suggestion })
        }
    }

    fun addSuggestionItem(item: String) {
        viewModelScope.launch(Dispatchers.IO) {
            suggestionRepo.addSuggestionItem(SuggestionModel(suggestion = item))
            getAllSuggestions()
        }
    }

}