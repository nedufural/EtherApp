package com.fastcon.etherapp.util

import com.facebook.FacebookSdk.getApplicationContext
import com.fastcon.etherapp.service.AuthManager
import java.util.*


class LogOutTimerTask : TimerTask() {
    override fun run() {
        AuthManager.authExpiredAndGoLogin(getApplicationContext())
    }
}
