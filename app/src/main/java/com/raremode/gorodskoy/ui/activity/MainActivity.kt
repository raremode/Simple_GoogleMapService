package com.raremode.gorodskoy.ui.activity

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.raremode.gorodskoy.R
import com.raremode.gorodskoy.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    val appSettings = "mySettings"
    val skipWelcomeScreen = "skip"
    val hideWelcomeScreenBoolean = true
    lateinit var mySharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNavController()
        setupNavGraph()
        setupBottomNavigation()
        var mySharedPreferences = getSharedPreferences(appSettings, Context.MODE_PRIVATE)
    }

    private fun setupNavController() {
        navHostFragment = supportFragmentManager.findFragmentById(
            R.id.amFragmentContainer
        ) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun setupNavGraph() {
        val auth = Firebase.auth
        navController.setGraph(R.navigation.main_navigation)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.welcomeFragment || destination.id == R.id.welcomeAccountLoginFragment || destination.id == R.id.welcomeAccountRegisterFragment || destination.id == R.id.welcomeAccountRecoveryFragment) {
                binding.amBottomNavigationView.visibility = View.GONE
            } else {
                binding.amBottomNavigationView.visibility = View.VISIBLE
               // binding.amBottomNavigationView.animation.duration = 4
            }
        }
//        val booleanState = mySharedPreferences.getBoolean(skipWelcomeScreen, false)
//
//        if(!booleanState) {
//            //...
//            //   layoutInflater.inflate(R.layout.fragment_map, container, false)
//            editor = mySharedPreferences.edit()
//            editor.putBoolean(skipWelcomeScreen, true)
//            editor.apply()
//        }
    }

    private fun setupBottomNavigation() {
        binding.amBottomNavigationView.setupWithNavController(navController)
    }

//        private fun actionWelcomeScreen() {
//            editor = mySharedPreferences.edit()
//            editor.putBoolean(skipWelcomeScreen, true)
//            editor.apply()
//        }


}