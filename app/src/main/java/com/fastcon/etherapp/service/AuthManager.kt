package com.fastcon.etherapp.service


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.facebook.FacebookSdk.getApplicationContext
import com.fastcon.etherapp.data.local.PrefUtils
import com.fastcon.etherapp.ui.home.HomeActivity
import com.fastcon.etherapp.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import java.util.concurrent.Executor


object AuthManager {
    fun authExpiredAndGoLogin(context: Context) {
        PrefUtils.setSignedIn(false)
        val intent = Intent(context, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)
        FirebaseAuth.getInstance().signOut();
    }

    fun logout(context: Context) {
        PrefUtils.setSignedIn(false)
         val intent = Intent(context, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
        FirebaseAuth.getInstance().signOut();
    }

    fun goHome(activity: Activity) {
        val intent = Intent(activity, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        activity.startActivity(intent)
    }

     fun bioAuth(fragment: FragmentActivity) {
        val executor: Executor = ContextCompat.getMainExecutor(getApplicationContext())
        val biometricPrompt = BiometricPrompt(fragment, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(
                        getApplicationContext(),
                        "Authentication error: $errString", Toast.LENGTH_SHORT
                    )
                        .show()
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(
                        getApplicationContext(),
                        "Authentication succeeded!", Toast.LENGTH_SHORT
                    )
                        .show()
                    val intent = Intent(fragment, HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    fragment.startActivity(intent)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(
                        getApplicationContext(), "Authentication failed",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            })

        val promptInfo: BiometricPrompt.PromptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for my app")
            .setSubtitle("Log in using your biometric credential")
            //.setNegativeButtonText("Use account password")
            .setDeviceCredentialAllowed(true)
            .build()
        biometricPrompt.authenticate(promptInfo)
    }

}
