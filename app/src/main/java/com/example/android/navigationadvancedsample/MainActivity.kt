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

package com.example.android.navigationadvancedsample

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.IntentSender
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.android.navigationadvancedsample.homescreen.PermissionUtils
import com.example.android.navigationadvancedsample.homescreen.PermissionUtils.PermissionDeniedDialog.Companion.newInstance
import com.example.android.navigationadvancedsample.homescreen.PermissionUtils.isPermissionGranted
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.material.bottomnavigation.BottomNavigationView

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import android.location.LocationManager
import com.google.android.gms.location.FusedLocationProviderClient
import androidx.annotation.NonNull

import com.google.android.gms.tasks.OnCompleteListener

import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.SupportMapFragment

/**
 * An activity that inflates a layout that has a [BottomNavigationView].
 */
class MainActivity : AppCompatActivity()/*, OnMapReadyCallback */{

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    @Override

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_host_container
        ) as NavHostFragment
        navController = navHostFragment.navController

        // Setup the bottom navigation view with navController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavigationView.setupWithNavController(navController)

        // Setup the ActionBar with navController and 3 top level destinations
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.titleScreen, R.id.leaderboard,  R.id.register)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        requestedOrientation =
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT // только вертикальная ориентация при использовании приложения
        supportActionBar!!.hide()

    }


}
