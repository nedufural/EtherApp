package com.fastcon.etherapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.fastcon.etherapp.R
import com.fastcon.etherapp.data.local.PrefUtils
import com.fastcon.etherapp.service.AuthenticationModel
import com.fastcon.etherapp.ui.home.HomeActivity
import com.fastcon.etherapp.ui.registration.RegistrationActivity
import com.fastcon.etherapp.util.functions.KeyBoardUtils.Companion.textLayoutHideKeyBoard
import com.fastcon.etherapp.util.functions.NetworkUtil.Companion.checkInternet
import com.fastcon.etherapp.util.views.FragmentUtils
import com.google.firebase.auth.FirebaseAuth

import kotlinx.android.synthetic.main.activity_login.*
import timber.log.Timber


class LoginActivity : AppCompatActivity() {
    private lateinit var loginModelViewModel: LoginViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginModelViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        animateView()
        email.editText?.setText("agwu@gmail.com")
        password.editText?.setText("Abc@123#")
        loginButtonEvent(submit)
        registerButtonEvent(register)
        clearAllSharedPreferences()
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

    override fun onPause() {
        super.onPause()
        finish()
    }

}
