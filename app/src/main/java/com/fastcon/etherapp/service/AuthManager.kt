package com.fastcon.etherapp.service


import android.app.Activity
import android.content.Context
import android.content.Intent
import com.fastcon.etherapp.data.local.PrefUtils
import com.fastcon.etherapp.ui.home.HomeActivity
import com.fastcon.etherapp.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth


object AuthManager {

    fun authExpiredAndGoLogin(context: Context) {
        PrefUtils.setSignedIn(false)
        val intent = Intent(context, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)
        FirebaseAuth.getInstance().signOut()
    }

    fun logout(context: Context) {
        PrefUtils.setSignedIn(false)
        val intent = Intent(context, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
        FirebaseAuth.getInstance().signOut()
    }

    fun goHome(activity: Activity) {
        val intent = Intent(activity, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        activity.startActivity(intent)
    }


}
