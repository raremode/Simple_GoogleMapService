package com.raremode.gorodskoy.ui.login

import android.content.Intent
import android.os.Bundle
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
import com.raremode.gorodskoy.databinding.FragmentAccountLoginBinding

class AccountLoginFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel
    private var _binding: FragmentAccountLoginBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    //Firebase references
    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var MAuth: FirebaseAuth? = null
    private val TAG = "AccountLoginFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAccountLoginBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backtoAccountFragment.setOnClickListener {
            findNavController().navigate(
                R.id.action_accountLoginFragment_to_navigation_account
            )
        }

        val usernameEditText = binding.username
        val passwordEditText = binding.password
        val loginButton = binding.login
        val forgotPassworded = binding.forgotPassword
        val gotoRegistration = binding.register
        val loadingProgressBar = binding.loading

        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference!!.child("Users")
        MAuth = FirebaseAuth.getInstance()

loginButton.setOnClickListener {
    val email = usernameEditText.text.toString()
    val password = passwordEditText.text.toString()

    MAuth!!.signInWithEmailAndPassword(email, password).addOnCompleteListener(
        OnCompleteListener<AuthResult?> { task ->
            if (task.isSuccessful) {
                Toast.makeText(
                    context,
                    "User logged in successfully",
                    Toast.LENGTH_SHORT
                ).show()

            } else {
                Toast.makeText(
                    context,
                    "Log in Error: " + task.exception!!.message,
                    Toast.LENGTH_SHORT
                ).show()
                Log.d(TAG, " |$email| " + task.exception!!.message)
            }
        })
}

        }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}