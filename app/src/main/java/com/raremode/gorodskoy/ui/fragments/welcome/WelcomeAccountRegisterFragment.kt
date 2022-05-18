package com.raremode.gorodskoy.ui.fragments.welcome

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.raremode.gorodskoy.R
import com.raremode.gorodskoy.databinding.FragmentWelcomeAccountRegisterBinding

class WelcomeAccountRegisterFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var _binding: FragmentWelcomeAccountRegisterBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    //Firebase references
    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var MAuth: FirebaseAuth? = null
    private val TAG = "WelcomeAccountRegisterFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentWelcomeAccountRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.skipWelcomeAccountLoginFragment2.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeAccountRegisterFragment_to_navigation_map)
        }

        val newUsernameEditText = binding.newUsername
        val newPasswordEditText = binding.newPassword
        val confirmPasswordEditText = binding.confirmNewPassword
        val registerButton = binding.signUp
        val loadingProgressBar = binding.loadingwelcomeregister

        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference!!.child("Users")
        MAuth = FirebaseAuth.getInstance()

        registerButton.setOnClickListener {
            val email = newUsernameEditText.text.toString()
            val password = newPasswordEditText.text.toString()
            val confirmpassword = confirmPasswordEditText.text.toString()
            if (password == confirmpassword) {
                MAuth!!.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                    OnCompleteListener<AuthResult?> { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                context,
                                "Пользователь успешно зарегистрирован!",
                                Toast.LENGTH_SHORT
                            ).show()
                            findNavController().navigate(R.id.action_welcomeAccountRegisterFragment_to_welcomeAccountLoginFragment)
                        } else {
                            Toast.makeText(
                                context,
                                "Ошибка регистрации: " + task.exception!!.message,
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.d(TAG, " |$email| " + task.exception!!.message)
                        }
                    })
            }
            else Toast.makeText(context, "Пароли не совпадают", Toast.LENGTH_SHORT).show()
        }
    }



}