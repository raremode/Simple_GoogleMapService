package com.raremode.gorodskoy.ui.fragments.welcome

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.raremode.gorodskoy.R
import com.raremode.gorodskoy.databinding.FragmentWelcomeBinding
import com.raremode.gorodskoy.ui.activity.MainActivity

class WelcomeFragment : Fragment() {

    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.skipWelcomeFragment.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_navigation_map)

        }
        binding.authorization.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_welcomeAccountLoginFragment)
        }
    }

//fun hideWelcomeScreen(): SharedPreferences.Editor{
//
//}

}