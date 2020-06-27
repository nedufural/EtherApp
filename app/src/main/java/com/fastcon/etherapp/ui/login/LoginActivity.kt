package com.fastcon.etherapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.fastcon.etherapp.R
import com.fastcon.etherapp.data.local.PrefUtils
import com.fastcon.etherapp.service.AuthenticationModel
import com.fastcon.etherapp.ui.home.HomeActivity
import com.fastcon.etherapp.ui.registration.RegistrationActivity
import com.fastcon.etherapp.util.Extensions.Companion.checkInternet
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.spinner_text.view.*
import timber.log.Timber

class LoginActivity : AppCompatActivity() {
    private lateinit var loginModelViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        animateView()
        Timber.i("VieModelProvider.of!")
        loginModelViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        email.editText?.setText("agwu@gmail.com")
        password.editText?.setText("Abc@123#")
        submit.setOnClickListener {
            if (!checkInternet(this)) {
                Toast.makeText(this, "Connect to internet to proceed!", Toast.LENGTH_LONG).show()
            } else {
                confirmInput()
            }
        }
        register.setOnClickListener {
            println("clicked")
            startActivity(Intent(this, RegistrationActivity::class.java))
        }

    }


    private fun animateView() {
        // username.startAnimation(AnimationUtils.loadAnimation(this, R.anim.animate_left_in))

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

    override fun onPause() {
        super.onPause()
        finish()
    }

    private fun confirmInput() {
        val loginModel = AuthenticationModel
        val mAuth = FirebaseAuth.getInstance()
        if (!loginModel.validateEmail(email)
            /** or !loginModel.validateUsername(username)*/
            or !loginModel.validatePassword(
                password
            )
        ) {
            return
        }
        var input = "Email: " + email?.editText?.text.toString()
        input += "\n"
        input += "Password: " + password?.editText?.text.toString()

        loginModelViewModel.authUser(
               this,
               mAuth,
               email.editText?.text.toString().trim(),
               password.editText?.text.toString().trim()
           )
        loginModelViewModel.loginMutableLiveData.observe(this, Observer { loginResult ->
            if (loginResult == "failure") {
                Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show()
                //finish()
            }

            PrefUtils.setSignedIn(true)
            startActivity(Intent(this, HomeActivity::class.java))
        })
    }


}
