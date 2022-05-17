package com.raremode.gorodskoy.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.raremode.gorodskoy.R
import com.raremode.gorodskoy.databinding.ActivityMainBinding
import com.raremode.gorodskoy.extensions.beGoneIf
import com.raremode.gorodskoy.extensions.setupKeyboardListener

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNavController()
        setupNavGraph()
        setupBottomNavigation()
        setupKeyboardListener { isShown ->
            if (navController.currentDestination?.id == R.id.welcomeFragment
                || navController.currentDestination?.id == R.id.welcomeAccountLoginFragment
                || navController.currentDestination?.id == R.id.welcomeAccountRecoveryFragment
                || navController.currentDestination?.id == R.id.welcomeAccountRegisterFragment
            ) {
                return@setupKeyboardListener
            } else {
                binding.amBottomNavigationView.beGoneIf(isShown)
            }
        }
    }

    private fun setupNavController() {
        navHostFragment = supportFragmentManager.findFragmentById(
            R.id.amFragmentContainer
        ) as NavHostFragment
        navController = navHostFragment.navController

    }

    private fun setupNavGraph() {
        val graph = navHostFragment.navController.navInflater.inflate(R.navigation.main_navigation)
        if (Firebase.auth.currentUser?.uid != null) {
            graph.setStartDestination(R.id.navigation_map)
        } else {
            graph.setStartDestination(R.id.welcomeFragment)
        }
        navController.setGraph(graph = graph, null)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.amBottomNavigationView.beGoneIf(
                destination.id == R.id.welcomeFragment
                        || destination.id == R.id.welcomeAccountRecoveryFragment
                        || destination.id == R.id.welcomeAccountRegisterFragment
                        || destination.id == R.id.welcomeAccountLoginFragment
            )
        }
    }

    private fun setupBottomNavigation() {
        binding.amBottomNavigationView.setupWithNavController(navController)
    }

}