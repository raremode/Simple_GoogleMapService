package com.raremode.gorodskoy.ui.fragments.map

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.google.android.gms.maps.model.LatLng
import com.raremode.gorodskoy.R
import com.raremode.gorodskoy.dao.marker.MarkerRepository
import com.raremode.gorodskoy.database.MarkerDao
import com.raremode.gorodskoy.database.MarkerDatabase
import com.raremode.gorodskoy.models.GarbageTypes
import com.raremode.gorodskoy.models.MarkerLocation
import com.raremode.gorodskoy.ui.models.FilterButtonModel
import com.raremode.gorodskoy.utils.JsonAssetsManager
import com.raremode.gorodskoy.utils.MarkersHandler
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

    private val _position = MutableLiveData<LatLng>()
    val position: LiveData<LatLng> = _position

    private lateinit var jsonAssetsManager: JsonAssetsManager
    private var garbageMarkers: List<MarkerLocation>? = null
    private lateinit var dao: MarkerDao
    private lateinit var markersHandler: MarkersHandler


    init {
        getAllMarkers()
        initFilterButtons()
    }

    private fun initFilterButtons() {
        val filterButtonItems = mutableListOf<FilterButtonModel>()
        filterButtonItems.add(FilterButtonModel( getStringAll(), GarbageTypes.All, true))
        filterButtonItems.add(FilterButtonModel(getStringPlastic(), GarbageTypes.PLASTIC, false))
        filterButtonItems.add(FilterButtonModel(getStringBatteries(), GarbageTypes.BATTERIES, false))
        filterButtonItems.add(FilterButtonModel(getStringGlass(), GarbageTypes.GLASS, false))
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

    private suspend fun getGarbageMarkers() {
        garbageMarkers = jsonAssetsManager.getMarkers()
            dao.addMarkers(garbageMarkers ?: emptyList())
        markersHandler.setGarbageMarkers(garbageMarkers)

    }

    fun getStringAll(): String {
        return getApplication<Application>().resources.getString(R.string.all)
    }

    fun getStringPlastic(): String {
        return getApplication<Application>().resources.getString(R.string.plastic)
    }

    fun getStringGlass(): String {
        return getApplication<Application>().resources.getString(R.string.glass)
    }

    fun getStringBatteries(): String {
        return getApplication<Application>().resources.getString(R.string.batteries)
    }
}