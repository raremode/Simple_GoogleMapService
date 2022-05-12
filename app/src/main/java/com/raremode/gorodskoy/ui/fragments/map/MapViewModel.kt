package com.raremode.gorodskoy.ui.fragments.map

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raremode.gorodskoy.dao.marker.MarkerRepository
import com.raremode.gorodskoy.database.MarkerDatabase
import com.raremode.gorodskoy.models.GarbageTypes
import com.raremode.gorodskoy.models.MarkerLocation
import com.raremode.gorodskoy.ui.models.FilterButtonModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MapViewModel(application: Application) : AndroidViewModel(application) {

    private val _markers = MutableLiveData<List<MarkerLocation>>()
    val markers: LiveData<List<MarkerLocation>> = _markers

    private val repository: MarkerRepository = MarkerRepository(
        markerDao = MarkerDatabase.getDatabase(application).markerDao()
    )

    private val _filterButtons = MutableLiveData<List<FilterButtonModel>>()
    val filterButtons: LiveData<List<FilterButtonModel>> = _filterButtons

    private var markersLocation = listOf<MarkerLocation>()

    init {
        getAllMarkers()
        initFilterButtons()
    }

    private fun initFilterButtons() {
        val filterButtonItems = mutableListOf<FilterButtonModel>()
        filterButtonItems.add(FilterButtonModel("Всё", GarbageTypes.All, true))
        filterButtonItems.add(FilterButtonModel("Пластик", GarbageTypes.PLASTIC, false))
        filterButtonItems.add(FilterButtonModel("Батарейки", GarbageTypes.BATTERIES, false))
        filterButtonItems.add(FilterButtonModel("Стекло", GarbageTypes.GLASS, false))
        _filterButtons.value = filterButtonItems
//        val adapter = FilterButtonsAdapter(filterButtonItems)
//        adapter.clickCallback = { filterButtonModel ->
//            setGarbageMarkers(filterButtonModel.type)
//        }
//        binding.apply {
//            fmRecyclerFilterButtons.layoutManager =
//                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//            fmRecyclerFilterButtons.adapter = adapter
//        }
    }

    fun setGarbageMarkers(type: GarbageTypes) {
        var markers = markersLocation.toList()
        markers = when (type) {
            GarbageTypes.BATTERIES -> markers.filter { it.garbageType == GarbageTypes.BATTERIES.type }
            GarbageTypes.GLASS -> markers.filter { it.garbageType == GarbageTypes.GLASS.type }
            GarbageTypes.PLASTIC -> markers.filter { it.garbageType == GarbageTypes.PLASTIC.type }
            else -> markers
        }
        _markers.value = markers
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
            _markers.postValue(markersFromDB)
            markersLocation = markersFromDB
        }
    }

}