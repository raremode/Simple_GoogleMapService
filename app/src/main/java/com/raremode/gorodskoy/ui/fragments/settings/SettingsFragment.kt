package com.raremode.gorodskoy.ui.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.raremode.gorodskoy.R
import com.raremode.gorodskoy.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backtoSettingsFragment.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_settings_to_navigation_account)
        }
        binding.gotoRegisterAccount.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_settings_to_accountRegisterFragment)
        }
        binding.gotoRecoverPassword.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_settings_to_accountRecoveryFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}