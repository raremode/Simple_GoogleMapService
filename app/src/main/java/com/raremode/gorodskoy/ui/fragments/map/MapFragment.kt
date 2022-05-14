package com.raremode.gorodskoy.ui.fragments.map

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.raremode.gorodskoy.ui.fragments.map.adapters.FilterButtonsAdapter
import com.raremode.gorodskoy.utils.LocationPermissionManager
import kotlinx.coroutines.launch

class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private var lastLocation: Location? = null
    private lateinit var locationPermissionManager: LocationPermissionManager
    private lateinit var locationService: FusedLocationProviderClient

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}