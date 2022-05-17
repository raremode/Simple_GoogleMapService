package com.raremode.gorodskoy.ui.fragments.map

import android.annotation.SuppressLint
import android.app.SearchManager
import android.database.Cursor
import android.database.MatrixCursor
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.provider.BaseColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.cursoradapter.widget.CursorAdapter
import androidx.cursoradapter.widget.SimpleCursorAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.raremode.gorodskoy.R
import com.raremode.gorodskoy.databinding.FragmentMapBinding
import com.raremode.gorodskoy.extensions.beGone
import com.raremode.gorodskoy.extensions.beVisible
import com.raremode.gorodskoy.extensions.hideKeyboard
import com.raremode.gorodskoy.ui.fragments.map.adapters.FilterButtonsAdapter
import com.raremode.gorodskoy.utils.LocationPermissionManager
import kotlinx.coroutines.launch

class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private var lastLocation: Location? = null
    private lateinit var locationPermissionManager: LocationPermissionManager
    private lateinit var locationService: FusedLocationProviderClient
    private var suggestions = emptyList<String>()

    private val mapViewModel: MapViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        locationService = LocationServices.getFusedLocationProviderClient(requireActivity())
        locationPermissionManager = LocationPermissionManager(this)
        initMap()
        setupFilterButtons()
    }

    @SuppressLint("MissingPermission")
    private fun requestLocationPermission(googleMap: GoogleMap? = null) {
        locationPermissionManager.requestLocationPermission { anyOfLocationPermissionGranted ->
            lifecycleScope.launch {
                if (anyOfLocationPermissionGranted) {
                    mapViewModel.location.observe(viewLifecycleOwner) { location ->
                        lastLocation = location
                    }
                    mapViewModel.runUpdatingLocation(locationService)
                    googleMap?.isMyLocationEnabled = true
                }
            }
        }
    }

    private fun setupFilterButtons() {
        val adapter = FilterButtonsAdapter()
        adapter.clickCallback = { button, position ->
            mapViewModel.setGarbageMarkers(button.type)
            mapViewModel.updateFilterButtonsState(position)
        }
        binding.apply {
            fmRecyclerFilterButtons.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            fmRecyclerFilterButtons.adapter = adapter
        }
        mapViewModel.filterButtons.observe(viewLifecycleOwner) { filterButtons ->
            adapter.setItems(filterButtons)
        }
    }

    private fun initMap() {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.fmMapContainer) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            binding.fmProgressBar.beGone()
            initSearchingMechanism(map = googleMap)
            requestLocationPermission(googleMap = googleMap)
            mapViewModel.markers.observe(viewLifecycleOwner) { markers ->
                googleMap.clear()
                markers.forEach {
                    googleMap.addMarker(it)
                }
            }
            binding.fmGpsRefresh.setOnClickListener {
                requestLocationPermission(googleMap = googleMap)
                lastLocation?.let {
                    googleMap.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(
                                it.latitude,
                                it.longitude
                            ), 15F
                        )
                    )
                }
            }
            googleMap.apply {
                uiSettings.apply {
                    isMyLocationButtonEnabled = false
                    isZoomControlsEnabled = false
                    isMapToolbarEnabled = false
                    isCompassEnabled = false
                }
            }
        }
    }

    private fun initSearchingMechanism(map: GoogleMap) {
        val geocoder = Geocoder(context)
        mapViewModel.suggestions.observe(viewLifecycleOwner) {
            suggestions = it
        }
        mapViewModel.getAllSuggestions()
        binding.apply {
            val from = arrayOf(SearchManager.SUGGEST_COLUMN_TEXT_1)
            val to = intArrayOf(R.id.siItemLabel)
            val cursorAdapter = SimpleCursorAdapter(
                context,
                R.layout.search_item,
                null,
                from,
                to,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
            )
            view?.findViewById<AutoCompleteTextView>(androidx.appcompat.R.id.search_src_text)?.threshold = 0
            fmSearchView.suggestionsAdapter = cursorAdapter
            fmSearchView.setOnSuggestionListener(object : SearchView.OnSuggestionListener {
                override fun onSuggestionSelect(position: Int): Boolean {
                    return false
                }

                @SuppressLint("Range")
                override fun onSuggestionClick(position: Int): Boolean {
                    val cursor = binding.fmSearchView.suggestionsAdapter.getItem(position) as Cursor
                    val selection =
                        cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1))
                    binding.fmSearchView.setQuery(selection, true)
                    return true
                }

            })
            fmSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    fmProgressBar.beVisible()
                    mapViewModel.addSuggestionItem(query)
                    runCatching {
                        geocoder.getFromLocationName("таганрог $query", 1)
                    }.onSuccess { addressList ->
                        val address = addressList?.firstOrNull()
                        if (address != null) {
                            val latLng = LatLng(address.latitude, address.longitude)
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                        } else {
                            Toast.makeText(
                                context,
                                resources.getString(R.string.map_fragment_address_not_found),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        fmProgressBar.beGone()
                        hideKeyboard()
                    }
                    return false
                }

                override fun onQueryTextChange(query: String?): Boolean {
                    cursorAdapter.changeCursor(setQueryRowSuggestions(query = query))
                    return true
                }
            })
        }
    }

    private fun setQueryRowSuggestions(query: String?): Cursor {
        val cursor =
            MatrixCursor(arrayOf(BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1))
        query?.let {
            suggestions.forEachIndexed { index, suggestion ->
                if (query.isEmpty()) {
                    cursor.addRow(arrayOf(index, suggestion))
                } else if (suggestion.contains(query, true)) {
                    cursor.addRow(arrayOf(index, suggestion))
                }
            }
        }
        return cursor
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}