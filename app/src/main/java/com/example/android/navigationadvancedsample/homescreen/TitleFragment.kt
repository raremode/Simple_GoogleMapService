/*
 * Copyright 2019, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.navigationadvancedsample.homescreen

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.android.navigationadvancedsample.R
import com.example.android.navigationadvancedsample.databinding.FragmentTitleBinding
import com.example.android.navigationadvancedsample.models.GarbageTypes
import com.example.android.navigationadvancedsample.utils.MarkersHandler
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.json.JSONObject
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


/**
 * Shows the main title screen with a button that navigates to [About].
 */
//TODO https://developer.android.com/guide/topics/ui/controls/radiobutton#:~:text=Radio%20buttons%20allow%20the%20user,side%2C%20use%20a%20spinner%20instead.
class TitleFragment : Fragment() {

    private lateinit var mapFragment: SupportMapFragment
    private val TAG = "MapActivity"

    private val FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
    private val COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
    private val LOCATION_PERMISSION_REQUEST_CODE = 1234
    private val DEFAULT_ZOOM = 14f

    //TODO ViewBinding
    private var _binding: FragmentTitleBinding? = null
    private val binding get() = _binding!!

    //vars
    private var locationPermissionsGranted = false
    private var map: GoogleMap? = null
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var prevLocationMarker: LatLng = LatLng(0.0, 0.0)
    private lateinit var myMarker: Marker
    private var isOpened = true
    private lateinit var markers: List<Marker>
    private lateinit var markersHandler: MarkersHandler

    lateinit var myTimer: Timer
    val uiHandler: Handler = Handler()
    var typeFilter: Int = 1 //1-всё, 2-пластик, 3-стекло, 4-батарейки. По умолчанию включается "1"

    var markerDataBase: ArrayList<LatLng> = ArrayList(5)
    private lateinit var databaseWork: DataBase_work
    private lateinit var baseJSON: JSONObject
    private lateinit var serverDataBase: ServerDataBase


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTitleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fillMarkerDataBase()
        getLocationPermission()
        databaseWork = DataBase_work(context)
        serverDataBase = ServerDataBase(context)
        serverDataBase.loadJSON()
        //  baseJSON = ServerDataBase(context)
        filterClickListener()

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(Runnable {
            updateLocation()
            Log.d(TAG, "UpdateLocation: updated well")
        }, 0, 10, TimeUnit.SECONDS) //постоянное обновление местоположения пользователя!
    }

    private fun fillMarkerDataBase() {
        var x: Double = 47.2
        var y: Double = 38.9
        var plus: Double = 0.005
        for (i in 0..5) {
            //  var newlatlng : LatLng = LatLng(x, y)
            // markerDataBase[i]= LatLng(x, y)
            markerDataBase.add(LatLng(x, y))
            x += plus
            y += plus
        }
    }

    private fun filterClickListener() {
        binding.apply {
            filterAll.setOnClickListener {
                //  buttonFilterAll.setBackgroundColor(resources.getColor(R.color.colorPrimary))
                filterAll.setBackgroundResource(R.drawable.ovalbuttons_ontap)
                filterPlastic.setBackgroundResource(R.drawable.ovalbuttons)
                filterGlass.setBackgroundResource(R.drawable.ovalbuttons)
                filterBatteries.setBackgroundResource(R.drawable.ovalbuttons)
                //...
                typeFilter = 1
                Log.d(TAG, "Type = " + typeFilter)
                setGarbageMarkers()
            }

            filterPlastic.setOnClickListener {
                //  buttonFilterAll.setBackgroundColor(resources.getColor(R.color.colorPrimary))
                filterPlastic.setBackgroundResource(R.drawable.ovalbuttons_ontap)
                filterAll.setBackgroundResource(R.drawable.ovalbuttons)
                filterGlass.setBackgroundResource(R.drawable.ovalbuttons)
                filterBatteries.setBackgroundResource(R.drawable.ovalbuttons)
                //...
                typeFilter = 2
                Log.d(TAG, "Type = " + typeFilter)
                setGarbageMarkers()
            }

            filterGlass.setOnClickListener {
                //  buttonFilterAll.setBackgroundColor(resources.getColor(R.color.colorPrimary))
                filterGlass.setBackgroundResource(R.drawable.ovalbuttons_ontap)
                filterAll.setBackgroundResource(R.drawable.ovalbuttons)
                filterPlastic.setBackgroundResource(R.drawable.ovalbuttons)
                filterBatteries.setBackgroundResource(R.drawable.ovalbuttons)
                //...
                typeFilter = 3
                Log.d(TAG, "Type = " + typeFilter)
                setGarbageMarkers()
            }

            filterBatteries.setOnClickListener {
                //  buttonFilterAll.setBackgroundColor(resources.getColor(R.color.colorPrimary))
                filterBatteries.setBackgroundResource(R.drawable.ovalbuttons_ontap)
                filterAll.setBackgroundResource(R.drawable.ovalbuttons)
                filterPlastic.setBackgroundResource(R.drawable.ovalbuttons)
                filterGlass.setBackgroundResource(R.drawable.ovalbuttons)
                //...
                typeFilter = 4
                Log.d(TAG, "Type = " + typeFilter)
                setGarbageMarkers()
            }
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
                        moveCamera(
                            LatLng(currentLocation.latitude, currentLocation.longitude),
                            DEFAULT_ZOOM
                        )
                        setMyselfMarker(currentLocation = currentLocation)
                    } else {
                        Log.d(TAG, "onComplete: current location is null")
                        Toast.makeText(
                            context,
                            "unable to get current location",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    binding.gpsRefresh.setOnClickListener {
                        val currentLocation = task.result
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
                        setMyselfMarker(currentLocation = currentLocation)
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
        map!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
    }

    private fun initMap() {
        Log.d(TAG, "initMap: initializing map")
        mapFragment = childFragmentManager.findFragmentById(R.id.mapsee) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            Log.d(TAG, "initMap: initializing map")
            Toast.makeText(context, "Map is ready", Toast.LENGTH_SHORT).show()
            Log.d(TAG, "onMapReady: map is ready")
            markersHandler = MarkersHandler(googleMap)
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
                map?.uiSettings?.isZoomControlsEnabled = true
                map?.uiSettings?.isMapToolbarEnabled = false
                map?.uiSettings?.isCompassEnabled = false

                setDefaultTypeMarkers()
                setGarbageMarkers()
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
            MarkerOptions().position(myLocationMarker).title("Ваше местоположение").icon(
                BitmapDescriptorFactory.fromResource(
                    R.drawable.marker
                )
            )
        myMarker = map?.addMarker(options)!!

    }

    private fun destroyMyselfMarker(prevLocationMarker: LatLng) {
        myMarker.remove()
    }

    private fun setDefaultTypeMarkers() {
        binding.apply {
            filterAll.setBackgroundResource(R.drawable.ovalbuttons_ontap)
            filterPlastic.setBackgroundResource(R.drawable.ovalbuttons)
            filterGlass.setBackgroundResource(R.drawable.ovalbuttons)
            filterBatteries.setBackgroundResource(R.drawable.ovalbuttons)
        }
        //...
        typeFilter = 1
        Log.d(TAG, "Type = " + typeFilter)
    }

    private fun setGarbageMarkers() {
        val plastic = LatLng(47.205242, 38.909498)
        val steklo = LatLng(47.206616, 38.904884)
        val batareika = LatLng(47.208862, 38.910366)
        val markers = serverDataBase.getMarkers()
        markersHandler.setGarbageMarkers(markers ?: listOf())
        binding.filterPlastic.setOnClickListener {
            map?.clear()
            val plasticMarkers = markers?.filter { it.garbageType == GarbageTypes.PLASTIC.type }
            markersHandler.setGarbageMarkers(plasticMarkers ?: emptyList())
        }
//        when (typeFilter) {
//            1 -> {
//                map?.clear()
//                getDeviceLocation()
//
//                /* for (i in 0..5){
//                    map?.addMarker(
//                        MarkerOptions()
//                            .position(markerDataBase[i])
//                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)))
//                } */ //тест малого массива точек на карту
//                var locationLength: Int = databaseWork.get_value()
//                Log.d(TAG, "length of database=" + locationLength)
//                if (locationLength != 0) {
//
//                    var locationList: ArrayList<LatLng> = databaseWork.getGarbageLocations(0)
//                    var locationType: ArrayList<Int> = databaseWork.getGarbageTypes()
//                    Log.d(TAG, "getDataBaseInfo: getting was successfully!")
//
//                    for (i in 0 until (locationType.size)) {
//                        when (locationType[i]) {
//                            1 -> {
//                                map?.addMarker(
//                                    MarkerOptions()
//                                        .position(locationList[i])
//                                        .icon(
//                                            BitmapDescriptorFactory.defaultMarker(
//                                                BitmapDescriptorFactory.HUE_RED
//                                            )
//                                        )
//                                )
//                            }
//                            2 -> {
//                                map?.addMarker(
//                                    MarkerOptions()
//                                        .position(locationList[i])
//                                        .icon(
//                                            BitmapDescriptorFactory.defaultMarker(
//                                                BitmapDescriptorFactory.HUE_ORANGE
//                                            )
//                                        )
//                                )
//                            }
//                            3 -> {
//                                map?.addMarker(
//                                    MarkerOptions()
//                                        .position(locationList[i])
//                                        .icon(
//                                            BitmapDescriptorFactory.defaultMarker(
//                                                BitmapDescriptorFactory.HUE_GREEN
//                                            )
//                                        )
//                                )
//                            }
//                        }
//                    }
//                }
//
//
//            }
//            2 -> { //eto plastic
//                map?.clear()
//                getDeviceLocation()
//
//                var locationLength: Int = databaseWork.get_value()
//                Log.d(TAG, "length of database=" + locationLength)
//                if (locationLength != 0) {
//
//                    var locationList: ArrayList<LatLng> = databaseWork.getGarbageLocations(1)
//
//                    for (i in 0 until (locationList.size)) {
//
//                        map?.addMarker(
//                            MarkerOptions()
//                                .position(locationList[i])
//                                .icon(
//                                    BitmapDescriptorFactory.defaultMarker(
//                                        BitmapDescriptorFactory.HUE_RED
//                                    )
//                                )
//                        )
//
//                    }
//
//                    Log.d(TAG, "getDataBaseInfo: getting was successfully!")
//                }
//
//            }
//            3 -> { //eto steklo
//                map?.clear()
//                getDeviceLocation()
//
//                var locationLength: Int = databaseWork.get_value()
//                Log.d(TAG, "length of database=" + locationLength)
//                if (locationLength != 0) {
//
//                    var locationList: ArrayList<LatLng> = databaseWork.getGarbageLocations(2)
//
//                    for (i in 0 until (locationList.size)) {
//
//                        map?.addMarker(
//                            MarkerOptions()
//                                .position(locationList[i])
//                                .icon(
//                                    BitmapDescriptorFactory.defaultMarker(
//                                        BitmapDescriptorFactory.HUE_ORANGE
//                                    )
//                                )
//                        )
//
//                    }
//
//                    Log.d(TAG, "getDataBaseInfo: getting was successfully!")
//                }
//            }
//            4 -> {
//                map?.clear()
//                getDeviceLocation()
//
//                var locationLength: Int = databaseWork.get_value()
//                Log.d(TAG, "length of database=" + locationLength)
//                if (locationLength != 0) {
//
//                    var locationList: ArrayList<LatLng> = databaseWork.getGarbageLocations(3)
//
//                    for (i in 0 until (locationList.size)) {
//
//                        map?.addMarker(
//                            MarkerOptions()
//                                .position(locationList[i])
//                                .icon(
//                                    BitmapDescriptorFactory.defaultMarker(
//                                        BitmapDescriptorFactory.HUE_GREEN
//                                    )
//                                )
//                        )
//
//                    }
//
//                    Log.d(TAG, "getDataBaseInfo: getting was successfully!")
//                }
//            }
//        }
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

    override fun onDestroyView() {
        super.onDestroyView()
        destroyMyselfMarker(prevLocationMarker)
        isOpened = false
        Log.d(TAG, "delete myself marker: deleting has been successfull!")
    }

}
