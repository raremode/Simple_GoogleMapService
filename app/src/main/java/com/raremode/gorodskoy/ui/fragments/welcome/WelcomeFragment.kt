package com.raremode.gorodskoy.ui.fragments.welcome

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.raremode.gorodskoy.R
import com.raremode.gorodskoy.databinding.FragmentWelcomeBinding
import com.raremode.gorodskoy.databinding.ActivityMainBinding
import com.raremode.gorodskoy.ui.activity.MainActivity

class WelcomeFragment : Fragment() {

    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!

    private var _binding2: ActivityMainBinding? = null
    private val binding2 get() = _binding2!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        binding2.amBottomNavigationView.isVisible = false
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.skipWelcomeFragment.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_navigation_map)
        }
    }

//    fun hideBottomNavigation() {
//        // bottom_navigation is BottomNavigationView
//        with(binding2.amBottomNavigationView) {
//            if (visibility == android.view.View.VISIBLE && alpha == 1f) {
//                animate()
//                    .alpha(0f)
//                    .withEndAction { visibility = android.view.View.GONE }
//            }
//        }
//    }

}