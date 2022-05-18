package com.raremode.gorodskoy.ui.fragments.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.raremode.gorodskoy.R
import com.raremode.gorodskoy.databinding.FragmentAccountBinding

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!
    private val accountViewModel: AccountViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        accountViewModel.loginState.observe(viewLifecycleOwner) { loginStateModel ->
            binding.apply {
                //gotoLogin.text = loginStateModel.buttonText
                NameField.text = loginStateModel.email
                gotoLogin.setOnClickListener() {
                    if (loginStateModel.isLogged) {
                        accountViewModel.logout()
                    } else {
                        findNavController().navigate(
                            R.id.action_navigation_account_to_accountLoginFragment
                        )
                    }
                }
            }
        }
        accountViewModel.checkLoginState()
    }

    companion object {
        const val NAME_STRING = "NAME_STRING"
        const val MAIL = "MAIL"
        const val PASS = "PASS"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}