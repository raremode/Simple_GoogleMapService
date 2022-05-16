package com.raremode.gorodskoy.ui.fragments.account

import android.app.Application
import android.content.res.loader.ResourcesProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.raremode.gorodskoy.R

class AccountViewModel  : ViewModel() {

    private val _loginState = MutableLiveData<LoginStateModel>()
    val loginState: LiveData<LoginStateModel> = _loginState

    fun checkLoginState() {
        if (Firebase.auth.uid != null) {
            _loginState.value = LoginStateModel(
                email = Firebase.auth.currentUser?.email ?: "",
                buttonText = "Выйти",
                isLogged = true
            )
        } else {
            _loginState.value = LoginStateModel(
                email = "Гость",
                buttonText = "Войти",
                isLogged = false
            )
        }
    }

    fun logout() {
        Firebase.auth.signOut()
        _loginState.value = LoginStateModel(
            email = "Гость",
            buttonText = "Войти",
            isLogged = false
        )
    }

//    private fun getStringSignIn(): String{
//        return getApplication<Application>().resources.getString(R.string.action_sign_in)
//    }
//
//    private fun getStringSignOut(): String{
//        return getApplication<Application>().resources.getString(R.string.action_sign_out)
//    }
//
//    private fun getStringGuest(): String{
//        return getApplication<Application>().resources.getString(R.string.guest)
//    }

}