package com.raremode.gorodskoy.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
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
        val auth = Firebase.auth
        navController.setGraph(R.navigation.main_navigation)
      //  binding.amBottomNavigationView.isVisible = false // скрытие меню навигации
    }

    private fun setupBottomNavigation() {
        binding.amBottomNavigationView.setupWithNavController(navController)
    }

    fun hideNavGraph()  {
        binding.amBottomNavigationView.isVisible = false // для скрытия меню навигации
    }

    fun showNavGraph()  {
        binding.amBottomNavigationView.isVisible = true // для показа меню навигации
    }

}