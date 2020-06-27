package com.fastcon.etherapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fastcon.etherapp.R
import com.fastcon.etherapp.ui.home.HomeActivity
import com.fastcon.etherapp.ui.login.LoginActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*      Handler().postDelayed({
                  if(PrefUtils.isSignedIn()) {

                      startActivity(
                          Intent(
                              this,
                              HomeActivity::class.java
                          )
                      )
                  }
                  else{
                  startActivity(
                      Intent(
                          this,
                          LoginActivity::class.java
                      ))}

              }, SPLASH_WAITING_TIME)
      */
        startActivity(
            Intent(
                this,
                LoginActivity::class.java
            )
        )
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}
