package com.fastcon.etherapp.ui.login

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fastcon.etherapp.data.model.entity.ExchangeRateEntity
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import timber.log.Timber

class LoginViewModel : ViewModel() {


     val loginMutableLiveData: MutableLiveData<String> = MutableLiveData()
    fun authUser(activity: Activity, mAuth: FirebaseAuth, email:String, password:String){
        val user = mAuth.currentUser
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener(activity, OnSuccessListener {task ->

                if (task != null) {
                    // Sign in success, update UI with the signed-in user's information
                    Timber.i( "signInWithEmail:success")

                    loginMutableLiveData.postValue(user?.email)

                } else {
                    // If sign in fails, display a message to the user.
                    Timber.i( "signInWithEmail:failure")
                    loginMutableLiveData.postValue("signInWithEmail:failure")
                }
            })
    }
}