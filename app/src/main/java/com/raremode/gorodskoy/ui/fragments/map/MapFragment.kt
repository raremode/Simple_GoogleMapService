package com.raremode.gorodskoy.ui.fragments.map

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.raremode.gorodskoy.R
import com.raremode.gorodskoy.database.MarkerDao
import com.raremode.gorodskoy.database.MarkerDatabase
import com.raremode.gorodskoy.databinding.ActivityMainBinding
import com.raremode.gorodskoy.databinding.FragmentMapBinding
import com.raremode.gorodskoy.extensions.bitmapDescriptorFromVector
import com.raremode.gorodskoy.models.GarbageTypes
import com.raremode.gorodskoy.models.MarkerLocation
import com.raremode.gorodskoy.models.Names
import com.raremode.gorodskoy.ui.activity.MainActivity
import com.raremode.gorodskoy.ui.fragments.map.adapters.FilterButtonsAdapter
import com.raremode.gorodskoy.ui.models.FilterButtonModel
import com.raremode.gorodskoy.utils.JsonAssetsManager
import com.raremode.gorodskoy.utils.MarkersHandler
import com.raremode.gorodskoy.utils.PermissionManager
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class MapFragment : Fragment() {

    private lateinit var mapFragment: SupportMapFragment
    private val TAG = "MapActivity"

    private val FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
    private val COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
    private val LOCATION_PERMISSION_REQUEST_CODE = 1234
    private val DEFAULT_ZOOM = 15f

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private var _binding2: ActivityMainBinding? = null
    private val binding2 get() = _binding2!!

    //vars
    private var locationPermissionsGranted = false
    private var map: GoogleMap? = null
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var prevLocationMarker: LatLng = LatLng(0.0, 0.0)
    private lateinit var myMarker: Marker
    private var isOpened = true
    private lateinit var markersHandler: MarkersHandler

    private lateinit var dao: MarkerDao
    private lateinit var jsonAssetsManager: JsonAssetsManager
    private var markers: List<MarkerLocation>? = null

    private val mapViewModel: MapViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        binding2.amBottomNavigationView.isVisible = true
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val permissionManager = PermissionManager(fragment = this)
        dao = MarkerDatabase.getDatabase(requireContext()).markerDao()
        permissionManager.requestLocationPermission {
            when (it) {
                PermissionManager.ACCESSED_FINE_LOCATION -> Toast.makeText(
                    context,
                    "Fine Location",
                    Toast.LENGTH_SHORT
                ).show()
                PermissionManager.ACCESSED_COARSE_LOCATION -> Toast.makeText(
                    context,
                    "Coarse Location",
                    Toast.LENGTH_SHORT
                ).show()
                PermissionManager.DENIED -> Toast.makeText(
                    context,
                    "Denied Location",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        getLocationPermission()
        setupFilterButtons()
        jsonAssetsManager = JsonAssetsManager(context)
        jsonAssetsManager.parseJsonFromAssets()
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate({
            updateLocation()
            Log.d(TAG, "UpdateLocation: updated well")
        }, 0, 2, TimeUnit.SECONDS) //постоянное обновление местоположения пользователя!

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate({
            map?.clear()
            Log.d(TAG, "ClearCache: cleared well")
        }, 0, 30, TimeUnit.SECONDS) // очистка кеша карты
    }

    private fun setupFilterButtons() {
        val adapter = FilterButtonsAdapter()
        adapter.clickCallback = { filterButtonModel ->
            mapViewModel.setGarbageMarkers(filterButtonModel.type)
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

    private fun getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting the devices current location")
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        try {
            if (locationPermissionsGranted) {
                val location = fusedLocationProviderClient?.lastLocation
                location?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "onComplete: found location!")
                        val currentLocation = task.result
                        if (currentLocation != null) {
                            moveCamera(
                                LatLng(currentLocation.latitude, currentLocation.longitude),
                                DEFAULT_ZOOM
                            )
                            setMyselfMarker(currentLocation = currentLocation)
                        }
                    } else {
                        Log.d(TAG, "onComplete: current location is null")
                        Toast.makeText(
                            context,
                            "unable to get current location",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    binding.fmGpsRefresh.setOnClickListener {
                        val currentLocation = task.result
                        if (currentLocation != null) {
                            destroyMyselfMarker(prevLocationMarker)
                            setMyselfMarker(currentLocation = currentLocation)
                            moveCamera(
                                LatLng(currentLocation.latitude, currentLocation.longitude),
                                DEFAULT_ZOOM
                            )
                            Log.d(TAG, "onComplete: location updated")
                            //  map?.animateCamera(CameraUpdateFactory.zoomTo(16f))
                        }
                    }

                }
            }
        } catch (e: SecurityException) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.message)
        }
    }

    private fun updateLocation() {
        Log.d(TAG, "updateLocation: getting the devices current location")
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        try {
            if (locationPermissionsGranted) {
                val location = fusedLocationProviderClient?.lastLocation
                location?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "onComplete: found NEW location!")
                        val currentLocation = task.result
                        if (currentLocation != null) {
                            setMyselfMarker(currentLocation = currentLocation)
                        }
                    } else {
                        Log.d(TAG, "onComplete: current location is null")
                        Toast.makeText(
                            context,
                            "unable to get current location",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e(TAG, "updateLocation: SecurityException: " + e.message)
        }
    }

    private fun moveCamera(latLng: LatLng, zoom: Float) {
        Log.d(
            TAG,
            "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude
        )
        map?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
    }

    private fun initMap() {
        Log.d(TAG, "initMap: initializing map")
        mapFragment =
            childFragmentManager.findFragmentById(R.id.fmMapContainer) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            Log.d(TAG, "initMap: initializing map")
            Toast.makeText(context, "Map is ready", Toast.LENGTH_SHORT).show()
            Log.d(TAG, "onMapReady: map is ready")
            markersHandler = MarkersHandler(googleMap)
            mapViewModel.markers.observe(viewLifecycleOwner) { markers ->
                map?.clear()
                markersHandler.setGarbageMarkers(markers)
            }
            map = googleMap
            if (locationPermissionsGranted) {
                getDeviceLocation()
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                )
                    map?.isMyLocationEnabled = true
                map?.uiSettings?.isMyLocationButtonEnabled = true
                map?.uiSettings?.isZoomControlsEnabled = false
                map?.uiSettings?.isMapToolbarEnabled = false
                map?.uiSettings?.isCompassEnabled = false
                getGarbageMarkers()
                map?.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.mapstyle))
            }
        }
    }

    private fun setMyselfMarker(currentLocation: Location) {
        val myLocationMarker = LatLng(currentLocation.latitude, currentLocation.longitude)
        prevLocationMarker = myLocationMarker
        //map?.apply {
        //     addMarker(MarkerOptions().position(myLocationMarker).title("Marker in My own location").icon(BitmapDescriptorFactory.fromResource(R.drawable.amu_bubble_mask))) //сделать нормальный значок метки пользователя
        //     moveCamera(CameraUpdateFactory.newLatLng(myLocationMarker))
        //}
        val options: MarkerOptions =
            MarkerOptions().position(myLocationMarker).title("Ваше местоположение")
                .icon(
                    context?.bitmapDescriptorFromVector(R.drawable.ic_map_marker)
                )
                //        myMarker = map?.addMarker(options)!!
map?.apply {
    addMarker(MarkerOptions().position(myLocationMarker).icon(context?.bitmapDescriptorFromVector(R.drawable.ic_map_marker)))
}

    }

    private fun destroyMyselfMarker(prevLocationMarker: LatLng) {
        if (::myMarker.isInitialized) {
            myMarker.remove()
        }
    }

    private fun getGarbageMarkers() {
        markers = jsonAssetsManager.getMarkers()
        lifecycleScope.launch {
            dao.addMarkers(markers ?: emptyList())
        }
        markersHandler.setGarbageMarkers(markers)

    }

    private fun getLocationPermission() {
        Log.d(TAG, "getLocationPermission: getting location permissions")
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    COURSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                locationPermissionsGranted = true
                initMap()
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            }
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                permissions,
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

//    private fun addSearchView(point: Names){
//        val names = markers?.filter { it.name == Names.All.point }
//Log.d(TAG, "$names")
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        destroyMyselfMarker(prevLocationMarker)
        _binding = null
        isOpened = false
        Log.d(TAG, "delete myself marker: deleting has been successfull!")
    }
}