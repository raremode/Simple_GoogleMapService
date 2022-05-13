package com.raremode.gorodskoy.utils

import android.Manifest
import android.content.pm.PackageManager
import androidx.fragment.app.Fragment
import com.fondesa.kpermissions.extension.permissionsBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LocationPermissionManager(
    private val fragment: Fragment
) {

    private val permissionManagerScope = CoroutineScope(Job() + Dispatchers.IO)

    fun requestLocationPermission(callback: (anyOfLocationPermissionGranted: Boolean) -> Unit) {
        val request = fragment.permissionsBuilder(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION).build()
        permissionManagerScope.launch {
            callback(isLocationPermissionGranted())
        }
        request.send()
    }

    fun isLocationPermissionGranted(): Boolean {
        return when (PackageManager.PERMISSION_GRANTED) {
            fragment.activity?.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                true
            }
            fragment.activity?.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) -> {
                true
            }
            else -> {
                false
            }
        }
    }
}