package com.fastcon.etherapp.service

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.fastcon.etherapp.data.local.PrefUtils
import com.fastcon.etherapp.ui.login.LoginActivity
import com.fastcon.etherapp.ui.home.HomeActivity


object AuthManager {
    fun authExpiredAndGoLogin(context: Context) {
        if(PrefUtils.hasLogin()){
            PrefUtils.expired()
            val intent = Intent(context, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            context.startActivity(intent)
        }


    }

    fun logout(activity: Activity){
        PrefUtils.logout()
        val intent = Intent(activity,
            LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        activity.startActivity(intent)
        activity.finish()
    }

    fun goHome(activity: Activity) {
       val intent = Intent(activity,HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        activity.startActivity(intent)
        activity.finish()
    }

}
