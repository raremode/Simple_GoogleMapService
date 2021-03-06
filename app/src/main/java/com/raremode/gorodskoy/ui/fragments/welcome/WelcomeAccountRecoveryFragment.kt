package com.raremode.gorodskoy.ui.fragments.welcome

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.raremode.gorodskoy.R
import com.raremode.gorodskoy.databinding.FragmentAccountLoginBinding
import com.raremode.gorodskoy.databinding.FragmentWelcomeAccountRecoveryBinding
import com.raremode.gorodskoy.databinding.FragmentNewsBinding

class WelcomeAccountRecoveryFragment : Fragment() {

    private var _binding: FragmentWelcomeAccountRecoveryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    //Firebase references
    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var MAuth: FirebaseAuth? = null
    private val TAG = "AccountRecoveryFragment"
    lateinit var mail:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentWelcomeAccountRecoveryBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backtoWelcomeAccountLoginFragment.setOnClickListener {
            findNavController().navigate(R.id.action_accountRecoveryFragment_to_accountLoginFragment)
        }

        val recoveringMail = binding.recoveryUsername

        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference!!.child("Users")
        MAuth = FirebaseAuth.getInstance()

        binding.recover.setOnClickListener {
            mail = recoveringMail.text.toString()
            if (TextUtils.isEmpty(mail)) {
                Toast.makeText(context, "???????????????? ???????????? ??????????", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "|$mail|")
            } else {
                MAuth!!.sendPasswordResetEmail(mail).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            context,
                            "???????????? ?????? ?????????? ???????????? ???????????????????? ???? ??????????",
                            Toast.LENGTH_SHORT
                        ).show()
                        findNavController().navigate(R.id.action_accountRecoveryFragment_to_accountLoginFragment)
                    } else {
                        Toast.makeText(
                            context,
                            "???????????? ???????????????????????????? ????????????????: " + task.exception!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d(TAG, " |$mail| " + task.exception!!.message)
                    }
                }
            }
        }
    }
}