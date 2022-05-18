package com.raremode.gorodskoy.ui.fragments.account.register

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
import com.raremode.gorodskoy.databinding.FragmentAccountRegisterBinding
import com.raremode.gorodskoy.extensions.beInvisible
import com.raremode.gorodskoy.extensions.beVisible

class AccountRegisterFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var _binding: FragmentAccountRegisterBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    //Firebase references
    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var MAuth: FirebaseAuth? = null
    private val TAG = "AccountRegisterFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAccountRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loadingregister.beInvisible()
        binding.backtoAccountLoginFragment.setOnClickListener {
            findNavController().navigate(R.id.action_accountRegisterFragment_to_accountLoginFragment)
        }

        val newUsernameEditText = binding.newUsername
        val newPasswordEditText = binding.newPassword
        val confirmPasswordEditText = binding.confirmNewPassword
        val registerButton = binding.signUp
        val loadingProgressBar = binding.loadingregister

        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference!!.child("Users")
        MAuth = FirebaseAuth.getInstance()

        registerButton.setOnClickListener {
            binding.loadingregister.beVisible()
            val email = newUsernameEditText.text.toString()
            val password = newPasswordEditText.text.toString()
            val confirmpassword = confirmPasswordEditText.text.toString()
            if (password == confirmpassword) {
                MAuth!!.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                    OnCompleteListener<AuthResult?> { task ->
                        if (task.isSuccessful) {
                            binding.loadingregister.beInvisible()
                            Toast.makeText(
                                context,
                                "Пользователь успешно зарегистрирован!",
                                Toast.LENGTH_SHORT
                            ).show()

                        } else {
                            binding.loadingregister.beInvisible()
                            Toast.makeText(
                                context,
                                "Ошибка регистрации: " + task.exception!!.message,
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.d(TAG, " |$email| " + task.exception!!.message)
                        }
                    })
            }
            else {
                binding.loadingregister.beInvisible()
                Toast.makeText(context, "Пароли не совпадают", Toast.LENGTH_SHORT).show()
            }
        }
    }



}