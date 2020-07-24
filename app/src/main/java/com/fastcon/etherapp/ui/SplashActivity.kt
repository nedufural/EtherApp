package com.fastcon.etherapp.ui


import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.fastcon.etherapp.R
import com.fastcon.etherapp.data.local.PrefUtils
import com.fastcon.etherapp.exception.NetworkExceptions
import com.fastcon.etherapp.service.AuthManager

import com.fastcon.etherapp.util.common.Commons.SPLASH_WAITING_TIME
import timber.log.Timber

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        splashFunction()
    }

    private fun splashFunction() {
        Handler().postDelayed({
            if (PrefUtils.isSignedIn()) {
                AuthManager.goHome(this)
            } else {
                AuthManager.authExpiredAndGoLogin(applicationContext)
            }
        }, SPLASH_WAITING_TIME)
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}
