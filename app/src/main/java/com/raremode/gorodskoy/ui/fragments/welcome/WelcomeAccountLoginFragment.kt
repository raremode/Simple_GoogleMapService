package com.raremode.gorodskoy.ui.fragments.welcome

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.raremode.gorodskoy.R
import com.raremode.gorodskoy.databinding.FragmentWelcomeAccountLoginBinding
import com.raremode.gorodskoy.ui.fragments.account.AccountFragment
import com.raremode.gorodskoy.ui.fragments.account.login.LoginViewModel

class WelcomeAccountLoginFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel
    private var _binding: FragmentWelcomeAccountLoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    //Firebase references
    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var MAuth: FirebaseAuth? = null
    private val TAG = "WelcomeAccountLoginFragment"

    lateinit var email: String
    lateinit var password: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentWelcomeAccountLoginBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.skipWelcomeAccountLoginFragment.setOnClickListener {
            findNavController().navigate(
                R.id.action_welcomeAccountLoginFragment_to_navigation_map
            )
        }

        binding.register.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeAccountLoginFragment_to_welcomeAccountRegisterFragment)
        }

        binding.forgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_accountLoginFragment_to_accountRecoveryFragment)
        }

        val usernameEditText = binding.username
        val passwordEditText = binding.password
        val loginButton = binding.login
        val forgotPassworded = binding.forgotPassword
        val gotoRegistration = binding.register
        val loadingProgressBar = binding.loadingwelcomelogin

        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference!!.child("Users")
        MAuth = FirebaseAuth.getInstance()

        loginButton.setOnClickListener {

            email = usernameEditText.text.toString()
            password = passwordEditText.text.toString()

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(context, "Неверный формат почты", Toast.LENGTH_SHORT).show()
            } else if (TextUtils.isEmpty(password)) {
                Toast.makeText(context, "Неверный формат пароля", Toast.LENGTH_SHORT).show()
            } else {
                MAuth!!.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            context,
                            "Вы успешно вошли в аккаунт!",
                            Toast.LENGTH_SHORT
                        ).show()


                        val loginedBundle = Bundle()
                        loginedBundle.putString(AccountFragment.NAME_STRING, email)
                        loginedBundle.putString(AccountFragment.MAIL, email)
                        loginedBundle.putString(AccountFragment.PASS, password)
                        findNavController().navigate(
                            R.id.action_welcomeAccountLoginFragment_to_navigation_map,
                            args = loginedBundle
                        )
                    } else {
                        Toast.makeText(
                            context,
                            "Ошибка входа: " + task.exception!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d(TAG, " |$email| " + task.exception!!.message)
                    }
                }
            }
        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}