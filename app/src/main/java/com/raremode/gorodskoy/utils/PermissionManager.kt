package com.raremode.gorodskoy.utils

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment

class PermissionManager(
    private val fragment: Fragment
) {

    companion object {
        const val ACCESSED_FINE_LOCATION = 0
        const val ACCESSED_COARSE_LOCATION = 1
        const val DENIED = -1
    }

    fun requestLocationPermission(callback: (Int) -> Unit) {
        fragment.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    callback(ACCESSED_FINE_LOCATION)
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    callback(ACCESSED_COARSE_LOCATION)
                }
                else -> {
                    callback(DENIED)
                }
            }
        }
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