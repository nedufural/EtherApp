package com.fastcon.etherapp.service

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.fastcon.etherapp.data.local.PrefUtils
import com.fastcon.etherapp.ui.login.LoginActivity
import com.fastcon.etherapp.ui.home.HomeActivity


object AuthManager {
    fun authExpiredAndGoLogin(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            context.startActivity(intent)
        }

    fun logout(context: Context){
        PrefUtils.setSignedIn(false)
        val intent = Intent(context, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
    }

    fun goHome(activity: Activity) {
       val intent = Intent(activity,HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        activity.startActivity(intent)
    }

}
