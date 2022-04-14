package com.raremode.gorodskoy.ui.fragments.map

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat

class GPSTracking(  //подключаем менеджер локаций
    var context: Context
) : LocationListener {
    // проверяем что GPS включен
    // проверяем что разрешение получено
    val location: Location?
        get() {

            // проверяем что разрешение получено
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(context, "Разрешение не предоставлено", Toast.LENGTH_LONG).show()
                return null
            }
            val lm =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager //подключаем менеджер локаций
            val isGPSEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
            var changings = true
            // проверяем что GPS включен
            if (isGPSEnabled && changings) {
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 600000, 100f, this)
                val l = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                changings = !changings
                return l
            } else {
                Toast.makeText(context, "Пожалуйста, включите GPS! =)", Toast.LENGTH_LONG).show()
            }
            return null
        }

    override fun onLocationChanged(location: Location) {}
    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
    override fun onProviderEnabled(provider: String) {}
    override fun onProviderDisabled(provider: String) {}
}