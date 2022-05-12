package com.raremode.gorodskoy.ui.fragments.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AccountViewModel : ViewModel() {

    private val _loginState = MutableLiveData<LoginStateModel>()
    val loginState: LiveData<LoginStateModel> = _loginState

    fun checkLoginState() {
        if (Firebase.auth.uid != null) {
            _loginState.value = LoginStateModel(
                email = Firebase.auth.currentUser?.email ?: "",
                buttonText = "Выход",
                isLogged = true
            )
        } else {
            _loginState.value = LoginStateModel(
                email = "Гость",
                buttonText = "Вход",
                isLogged = false
            )
        }
    }

    fun logout() {
        Firebase.auth.signOut()
        _loginState.value = LoginStateModel(
            email = "Гость",
            buttonText = "Вход",
            isLogged = false
        )
    }

}