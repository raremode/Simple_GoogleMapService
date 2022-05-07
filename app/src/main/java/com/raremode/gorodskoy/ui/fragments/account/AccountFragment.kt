package com.raremode.gorodskoy.ui.fragments.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.raremode.gorodskoy.R
import com.raremode.gorodskoy.databinding.FragmentAccountBinding
import com.raremode.gorodskoy.ui.fragments.account.login.AccountLoginFragment


class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    private var checkLogin = false
    private var username: String? = null
    private var password: String? = null

    //Firebase references
    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var MAuth: FirebaseAuth? = null
    private val TAG = "AccountFragment"

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

        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference!!.child("Users")
        MAuth = FirebaseAuth.getInstance()

        autoLogin(MAIL, PASS)

        var accountLoginFragment = AccountLoginFragment()
        val nameOfUser = arguments?.getString(NAME_STRING) ?: "Гость"
        username = nameOfUser
        binding.NameField.setText(username)
        if(NAME_STRING != "NAME_STRING" && NAME_STRING != "Гость") {binding.gotoLogin.visibility=View.INVISIBLE}
        binding.gotoLogin.setOnClickListener() {
            findNavController().navigate(
                R.id.action_navigation_account_to_accountLoginFragment
            )
        }
    }

    override fun onStart() {
        super.onStart()
        if(NAME_STRING != "NAME_STRING" && NAME_STRING != "Гость") {binding.gotoLogin.visibility=View.INVISIBLE}
    }

    fun autoLogin(mail:String, pass:String){
if(MAIL != "MAIL" && PASS != "PASS") {
    MAuth!!.signInWithEmailAndPassword(MAIL, PASS)
}
    }

    companion object{
        const val NAME_STRING = "NAME_STRING"
        const val MAIL = "MAIL"
        const val PASS = "PASS"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}