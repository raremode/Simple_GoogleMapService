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
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.android.navigationadvancedsample.MainActivity
import com.example.android.navigationadvancedsample.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task






/**
 * Shows the main title screen with a button that navigates to [About].
 */
class Title : Fragment() {

    private lateinit var mContext : Context
    private lateinit var aContext: Activity
    private lateinit var mapFragment: SupportMapFragment
    private val TAG = "MapActivity"

    private val FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
    private val COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
    private val LOCATION_PERMISSION_REQUEST_CODE = 1234
    private val DEFAULT_ZOOM = 15f

    //vars
    private var mLocationPermissionsGranted = false
    private var mMap: GoogleMap? = null
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        aContext = activity as Activity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_title, container, false)
getLocationPermission()
        view.findViewById<Button>(R.id.about_btn).setOnClickListener {
            findNavController().navigate(R.id.action_title_to_about)
        }
        return view
    }

      private fun getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting the devices current location")
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext)
        try {
            if (mLocationPermissionsGranted) {
                val location: Task<*> = mFusedLocationProviderClient!!.getLastLocation()
                location.addOnCompleteListener {
                    fun onComplete(task: Task<*>) {
                        if (task.isSuccessful) {
                            Log.d(TAG, "onComplete: found location!")
                            val currentLocation = task.result as Location
                            moveCamera(
                                LatLng(currentLocation.latitude, currentLocation.longitude),
                                DEFAULT_ZOOM
                            )
                        } else {
                            Log.d(TAG, "onComplete: current location is null")
                            Toast.makeText(
                                mContext,
                                "unable to get current location",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.message)
        }
    }

    private fun moveCamera(latLng: LatLng, zoom: Float) {
        Log.d(
            TAG,
            "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude
        )
        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
    }

    private fun initMap() {
        Log.d(TAG, "initMap: initializing map")
        mapFragment = (childFragmentManager.findFragmentById(R.id.mapsee) as SupportMapFragment?)!!

        mapFragment.getMapAsync {
            Log.d(TAG, "initMap: initializing map")
            @Override
            fun onMapReady(googleMap: GoogleMap){
                Toast.makeText(mContext, "Map is ready", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "onMapReady: map is ready")
                mMap = googleMap
                setMarker()
                if (mLocationPermissionsGranted) {
                    getDeviceLocation()
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            mContext,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return
                    }
                    mMap!!.isMyLocationEnabled = true
                    mMap!!.uiSettings.isMyLocationButtonEnabled = false
                }
            }
        }
    }

    private fun setMarker(){
        val sydney = LatLng(-33.852, 151.211)
        mMap!!.addMarker(
            MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney")
        )
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    private fun getLocationPermission() {
        Log.d(TAG, "getLocationPermission: getting location permissions")
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (ContextCompat.checkSelfPermission(
                mContext,
                FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            if (ContextCompat.checkSelfPermission(
                    mContext,
                    COURSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                mLocationPermissionsGranted = true
                initMap()
            } else {
                ActivityCompat.requestPermissions(
                    aContext,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            }
        } else {
            ActivityCompat.requestPermissions(
                aContext,
                permissions,
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
      //  super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d(TAG, "onRequestPermissionsResult: called.")
        mLocationPermissionsGranted = false
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty()) {
                    var i = 0
                    while (i < grantResults.size) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false
                            Log.d(TAG, "onRequestPermissionsResult: permission failed")
                            return
                        }
                        i++
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted")
                    mLocationPermissionsGranted = true
                    //initialize our map
                    initMap()
                }
            }
        }
    }



}
