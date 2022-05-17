package com.raremode.gorodskoy.ui.activity

import android.graphics.Rect
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
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

class MainActivity : AppCompatActivity(), OnKeyboardVisibilityListener {

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
        setupKeyboardVisibilityListener(this)
    }

    private fun setupNavController() {
        navHostFragment = supportFragmentManager.findFragmentById(
            R.id.amFragmentContainer
        ) as NavHostFragment
        navController = navHostFragment.navController

    }

    private fun setupNavGraph() {
        val graph = navHostFragment.navController.navInflater.inflate(R.navigation.main_navigation)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.amBottomNavigationView.beGoneIf(
                destination.id == R.id.welcomeFragment
                        || destination.id == R.id.welcomeAccountRecoveryFragment
                        || destination.id == R.id.welcomeAccountRegisterFragment
                        || destination.id == R.id.welcomeAccountLoginFragment
            )
            if (Firebase.auth.currentUser?.uid != null) {
                graph.setStartDestination(R.id.navigation_map)
            } else {
                graph.setStartDestination(R.id.welcomeFragment)
            }
            navController.setGraph(graph = graph, null)
        }
    }

    private fun setupBottomNavigation() {
        binding.amBottomNavigationView.setupWithNavController(navController)
    }

    private fun setupKeyboardVisibilityListener(onKeyboardVisibilityListener: OnKeyboardVisibilityListener) {
        val parentView = (findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0)
        parentView.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            private var alreadyOpen = false
            private val defaultKeyboardHeightDP = 100
            private val EstimatedKeyboardDP =
                defaultKeyboardHeightDP + 48
            private val rect: Rect = Rect()
            override fun onGlobalLayout() {
                val estimatedKeyboardHeight = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    EstimatedKeyboardDP.toFloat(),
                    parentView.resources.displayMetrics
                )
                    .toInt()
                parentView.getWindowVisibleDisplayFrame(rect)
                val heightDiff: Int = parentView.rootView.height - (rect.bottom - rect.top)
                val isShown = heightDiff >= estimatedKeyboardHeight
                if (isShown == alreadyOpen) {
                    return
                }
                alreadyOpen = isShown
                onKeyboardVisibilityListener.onVisibilityChanged(isShown)
            }
        })
    }

    override fun onVisibilityChanged(visible: Boolean) {
        if (navController.currentDestination?.id == R.id.welcomeFragment
            || navController.currentDestination?.id == R.id.welcomeAccountLoginFragment
            || navController.currentDestination?.id == R.id.welcomeAccountRecoveryFragment
            || navController.currentDestination?.id == R.id.welcomeAccountRegisterFragment
        ) {
            return
        } else {
            binding.amBottomNavigationView.beGoneIf(visible)
        }
    }

}