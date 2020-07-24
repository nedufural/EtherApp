package com.fastcon.etherapp.ui.login

import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.facebook.FacebookSdk
import com.fastcon.etherapp.R
import com.fastcon.etherapp.data.local.PrefUtils
import com.fastcon.etherapp.data.remote.entity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import timber.log.Timber


class LoginViewModel : ViewModel() {


    private var errorLiveData: MutableLiveData<String>? = null
    fun getLoginError(): LiveData<String>? {
        if (errorLiveData == null) {
            errorLiveData = MutableLiveData()
        }
        return errorLiveData
    }

    private var loginMutableLiveData: MutableLiveData<String>? = null
    fun getLoginResult(): LiveData<String>? {
        if (loginMutableLiveData == null) {
            loginMutableLiveData = MutableLiveData()
        }
        return loginMutableLiveData
    }


    fun authUser(mAuth: FirebaseAuth, email: String, password: String) {
        val user = mAuth.currentUser
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    loginMutableLiveData?.postValue(user?.email)
                    user?.email?.let { PrefUtils.setEmail(it) }
                }
                else{
                    errorLiveData?.postValue("error")
                }
            }
    }


}