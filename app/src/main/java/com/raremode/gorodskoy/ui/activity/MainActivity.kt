package com.raremode.gorodskoy.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.raremode.gorodskoy.R
import com.raremode.gorodskoy.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNavController()
        setupNavGraph()
        setupBottomNavigation()
    }

    private fun setupNavController() {
        navHostFragment = supportFragmentManager.findFragmentById(
            R.id.amFragmentContainer
        ) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun setupNavGraph() {
        navController.setGraph(R.navigation.main_navigation)
    }

    private fun setupBottomNavigation() {
        binding.amBottomNavigationView.setupWithNavController(navController)
    }

}