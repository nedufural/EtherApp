package com.fastcon.etherapp.ui.login

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.facebook.FacebookSdk
import com.fastcon.etherapp.R
import com.fastcon.etherapp.data.local.PrefUtils
import com.fastcon.etherapp.service.AuthManager
import com.fastcon.etherapp.service.AuthenticationModel
import com.fastcon.etherapp.ui.home.HomeActivity
import com.fastcon.etherapp.ui.registration.RegistrationActivity
import com.fastcon.etherapp.util.functions.KeyBoardUtils.Companion.textLayoutHideKeyBoard
import com.fastcon.etherapp.util.functions.NetworkUtil.Companion.checkInternet
import com.google.firebase.auth.FirebaseAuth
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_login.*
import timber.log.Timber
import java.util.concurrent.Executor


class LoginActivity : AppCompatActivity() {
    private lateinit var loginModelViewModel: LoginViewModel
    private var isEnabled: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginModelViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        animateView()
        email.editText?.setText(PrefUtils.getEmail())
        password.editText?.setText(PrefUtils.getPassword())
        loginButtonEvent(submit)
        registerButtonEvent(register)
        clearAllSharedPreferences()
        bioAnimation()


        val biometricManager = BiometricManager.from(this)
        var bioMetricExist = checkBioMetricFeatureExists(biometricManager)
        if (bioMetricExist) {
            if (PrefUtils.getEnableBio()) {
                enable_bio_id.isChecked = true
                bioAuth(this)
            } else {
                enable_bio_id.isChecked = false
                //todo clear use and psw
            }
        }
        enable_bio_id.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked && bioMetricExist) {

                if (email.editText?.text.toString().trim().isEmpty() ||
                    password.editText?.text.toString().trim().isEmpty()
                ) {
                    PrefUtils.setEnableBio(false)
                    Toasty.custom(
                        applicationContext,
                        "Fill-in your login credentials first!",
                        R.drawable.ic_baseline_warning_24,
                        R.color.colorPrimaryDark,
                        5,
                        true,
                        true
                    ).show()
                } else {
                    PrefUtils.setEmail(email.editText?.text.toString().trim())
                    PrefUtils.setPassword(password.editText?.text.toString().trim())
                    PrefUtils.setEnableBio(true)
                }
            } else if (isChecked && !bioMetricExist) {
                Toasty.custom(
                    applicationContext,
                    "Bio-metric not supported by your device!",
                    R.drawable.ic_baseline_warning_24,
                    R.color.colorPrimaryDark,
                    5,
                    true,
                    true
                ).show()
                PrefUtils.setEnableBio(false)
                enable_bio_id.isChecked = false
            }
        }
    }


    private fun checkBioMetricFeatureExists(biometricManager: BiometricManager): Boolean {
        when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS ->
                // println( "App can authenticate using biometrics.")
                return true
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                return false
            //println("No biometric features available on this device.")
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                return false
            // println("Biometric features are currently unavailable.")
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED ->
                return false
            //println("The user hasn't associated any biometric credentials with their account.")
        }
        return true
    }

    private fun registerButtonEvent(button: Button) {
        button.setOnClickListener {

            startActivity(Intent(this, RegistrationActivity::class.java))
        }
    }

    private fun loginButtonEvent(button: Button) {
        button.setOnClickListener {
            PrefUtils.setEmail(email?.editText?.text.toString())
            textLayoutHideKeyBoard(this, password)
            if (!checkInternet(this)) {
                Toast.makeText(this, getString(R.string.check_network), Toast.LENGTH_LONG).show()
            } else {
                progress_layout.visibility = View.VISIBLE
                confirmInput()
                loginSuccess()
                loginError()
            }
        }
    }

    private fun clearAllSharedPreferences() {
        PrefUtils.clearBTCKey()
        PrefUtils.clearEthKey()
        PrefUtils.clearBitcoinAddress()
        PrefUtils.clearEtherAddress()
        PrefUtils.clearUserName()
        PrefUtils.clearDeviceToken()
        PrefUtils.clearReceiverToken()

    }


    private fun animateView() {


        password.startAnimation(
            AnimationUtils.loadAnimation(
                applicationContext,
                R.anim.animate_right_in
            )
        )
        email.startAnimation(
            AnimationUtils.loadAnimation(
                applicationContext,
                R.anim.animate_right_in
            )
        )
        submit.startAnimation(
            AnimationUtils.loadAnimation(
                applicationContext,
                R.anim.animate_left_in
            )
        )
        img_logo.startAnimation(
            AnimationUtils.loadAnimation(
                applicationContext,
                R.anim.animate_scaling
            )
        )
        register.startAnimation(
            AnimationUtils.loadAnimation(
                applicationContext,
                R.anim.animate_down_up
            )
        )

    }

    private fun bioAnimation() {
        bio_image.setBackgroundResource(R.drawable.animate_biometric)
        val wifiAnimation = bio_image.background as AnimationDrawable
        wifiAnimation.start()
    }

    private fun confirmInput() {
        val loginModel = AuthenticationModel
        val mAuth = FirebaseAuth.getInstance()
        if (!loginModel.validateEmail(email) or !loginModel.validatePassword(password)) {
            return
        }

        loginAuthenticationMethod(mAuth)
    }

    private fun loginAuthenticationMethod(mAuth: FirebaseAuth) {
        loginModelViewModel.authUser(
            mAuth,
            email.editText?.text.toString().trim(),
            password.editText?.text.toString().trim()
        )
    }

    private fun loginSuccess() {
        loginModelViewModel.getLoginResult()?.observe(this, Observer {
            Timber.i(it)
            PrefUtils.setSignedIn(true)
            progress_layout.visibility = View.GONE
            startActivity(Intent(this, HomeActivity::class.java))
            error_alert.visibility = View.GONE
        })
    }

    private fun loginError() {
        loginModelViewModel.getLoginError()?.observe(this, Observer {
            progress_layout.visibility = View.GONE
            error_alert.visibility = View.VISIBLE
        })
    }

    fun bioAuth(fragment: FragmentActivity) {
        val executor: Executor = ContextCompat.getMainExecutor(FacebookSdk.getApplicationContext())
        val biometricPrompt = BiometricPrompt(fragment, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    val intent = Intent(fragment, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    fragment.startActivity(intent)
                    PrefUtils.setEnableBio(false)
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    loginAuthenticationMethod(FirebaseAuth.getInstance())
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(
                        FacebookSdk.getApplicationContext(), "Authentication failed",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            })

        val promptInfo: BiometricPrompt.PromptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Login to EtherApp via biometric")
            .setSubtitle("Log in using your biometric credential")
            //.setNegativeButtonText("Login via username and password.")
            .setDeviceCredentialAllowed(true)
            .build()
        biometricPrompt.authenticate(promptInfo)
    }

    override fun onPause() {
        super.onPause()
        finish()
    }

}
