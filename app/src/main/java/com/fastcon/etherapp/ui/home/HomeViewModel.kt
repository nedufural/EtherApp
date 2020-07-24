package com.fastcon.etherapp.ui.home

import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.facebook.FacebookSdk.getApplicationContext
import com.fastcon.etherapp.R
import com.fastcon.etherapp.data.remote.entity.User
import com.google.firebase.database.*
import timber.log.Timber


class HomeViewModel : ViewModel() {

    private var userCredentials: MutableLiveData<User>? = null
    fun getUserCredentials(): LiveData<User>? {
        if (userCredentials == null) {
            userCredentials = MutableLiveData()
        }
        return userCredentials
    }

    private var databaseErrorRetrieving: MutableLiveData<String>? = null
    fun getDatabaseErrorRetrieving(): LiveData<String>? {
        if (databaseErrorRetrieving == null) {
            databaseErrorRetrieving = MutableLiveData()
        }
        return databaseErrorRetrieving
    }

    fun retrieveUserDetails(myRef: DatabaseReference) {
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)
                Timber.v("$user")
                userCredentials?.value = user
            }
            override fun onCancelled(@NonNull databaseError: DatabaseError) {
                databaseErrorRetrieving?.value = getApplicationContext().getString(R.string.database_error)
            }
        })
    }
}

