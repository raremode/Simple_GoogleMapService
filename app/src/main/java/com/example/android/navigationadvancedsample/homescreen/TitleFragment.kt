package com.example.android.navigationadvancedsample.homescreen

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.android.navigationadvancedsample.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

/**
 * Shows the main title screen with a button that navigates to [About].
 */
//TODO я переименовал из Title в TitleFragment. Если это фрагмент, то добавляй это обозначение, иначе в большом проекте запутаешься.
class TitleFragment : Fragment() {

    private lateinit var mapFragment: SupportMapFragment
    private val TAG = "MapActivity"

    private val FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
    private val COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
    private val LOCATION_PERMISSION_REQUEST_CODE = 1234
    private val DEFAULT_ZOOM = 15f

    //vars
    private var locationPermissionsGranted = false
    private var map: GoogleMap? = null
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
//        view.findViewById<Button>(R.id.about_btn).setOnClickListener {
//            findNavController().navigate(R.id.action_title_to_about)
//        }
        return inflater.inflate(R.layout.fragment_title, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getLocationPermission()
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
                        setMarker(currentLocation = currentLocation)
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
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.message)
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
        mapFragment = childFragmentManager.findFragmentById(R.id.ftMapGoogle) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            Log.d(TAG, "initMap: initializing map")
            Toast.makeText(context, "Map is ready", Toast.LENGTH_SHORT).show()
            Log.d(TAG, "onMapReady: map is ready")
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
                    map?.uiSettings?.isMyLocationButtonEnabled = false
                }
        }
    }

    private fun setMarker(currentLocation: Location) {
        val myLocationMarker = LatLng(currentLocation.latitude, currentLocation.longitude)
        map?.apply {
            addMarker(MarkerOptions().position(myLocationMarker).title("Marker in My own location"))
            moveCamera(CameraUpdateFactory.newLatLng(myLocationMarker))
        }
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

}
