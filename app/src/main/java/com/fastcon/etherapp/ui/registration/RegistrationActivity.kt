package com.fastcon.etherapp.ui.registration

import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.fastcon.etherapp.R
import com.fastcon.etherapp.base.BaseActivity
import com.fastcon.etherapp.data.local.PrefUtils
import com.fastcon.etherapp.databinding.ActivityRegistrationBinding
import com.fastcon.etherapp.service.AuthenticationModel
import com.fastcon.etherapp.ui.home.HomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_registration.*

class RegistrationActivity : BaseActivity<ActivityRegistrationBinding>() {
    private lateinit var regViewModel: RegistrationViewModel
    private lateinit var database: DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mAuthListener: FirebaseAuth.AuthStateListener

    override fun getLayoutId(): Int {
        return R.layout.activity_registration
    }

    override fun initData() {
        regViewModel = ViewModelProviders.of(this).get(RegistrationViewModel::class.java)

    }


    override fun initEvent() {

        submit_reg.setOnClickListener {
            val username = user_reg_name.editText?.text.toString().trim()
            val regEmail = user_reg_email.editText?.text.toString().trim()
            val regPassword = user_reg_password_first.editText?.text.toString().trim()
            val regPassword2 = user_reg_password_second.editText?.text.toString().trim()

            if (regPassword != regPassword2) {
                showToast(this, "Passwords are not the same !!")
            } else {
                if (AuthenticationModel.validateUsername(user_reg_name)
                    && (AuthenticationModel.validateEmail(user_reg_email)
                            && AuthenticationModel.validatePassword(user_reg_password_first)
                            && AuthenticationModel.validatePassword(user_reg_password_second))
                ) {
                    regViewModel.createEthereumWallet()
                    regViewModel.createWallet.observe(this, Observer { ethAddress ->

                        PrefUtils.ethereumAddress(ethAddress)
                    })


                    regViewModel.bitcoinWallet()
                    regViewModel.createBitcoinWallet.observe(this, Observer { btcAddress ->
                        PrefUtils.btcAddress(btcAddress.toString())
                    })

                    regViewModel.ethPrivateKey.observe(this, Observer { ethPrivate ->
                        PrefUtils.saveEthKey(ethPrivate.toString())

                    })
                    regViewModel.btcPrivateKey.observe(this, Observer { btcPrivate ->
                        PrefUtils.saveBitKey(btcPrivate.toString())

                    })

                    regViewModel.emailPasswordRegistration(
                        this,
                        username,
                        regEmail,
                        regPassword,
                        "PrefUtils.getEthKey()",
                        PrefUtils.getEtherAddress(),
                        "bitcoinPrimaryKey",
                        PrefUtils.getBitcoinAddress()
                    )
                } else {
                    showToast(this, "Registration failed !!")
                }
            }
            regViewModel.registrationResult.observe(this, Observer { results ->

                showToast(this, results)
                startActivity(Intent(this, HomeActivity::class.java))
            })


        }
    }

}

