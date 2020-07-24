package com.fastcon.etherapp.ui.registration

import android.content.Intent
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.fastcon.etherapp.R
import com.fastcon.etherapp.base.BaseActivity
import com.fastcon.etherapp.data.local.PrefUtils
import com.fastcon.etherapp.databinding.ActivityRegistrationBinding
import com.fastcon.etherapp.service.AuthenticationModel
import com.fastcon.etherapp.ui.dialogs.RegisterSuccessDialog
import com.fastcon.etherapp.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.toolbar_activity.*
import java.util.logging.Handler

class RegistrationActivity : BaseActivity<ActivityRegistrationBinding>() {
    private lateinit var regViewModel: RegistrationViewModel
    private lateinit var database: DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mAuthListener: FirebaseAuth.AuthStateListener

    override fun getLayoutId(): Int {
        return R.layout.activity_registration
    }

    override fun initData() {
        profile_text.visibility = View.GONE
        regViewModel = ViewModelProviders.of(this).get(RegistrationViewModel::class.java)

    }


    override fun initEvent() {

        submit_reg.setOnClickListener {
            progress_layout.visibility = View.VISIBLE
            val username = user_reg_name.editText?.text.toString().trim()
            val regEmail = user_reg_email.editText?.text.toString().trim()
            val regPassword = user_reg_password_first.editText?.text.toString().trim()
            val regPassword2 = user_reg_password_second.editText?.text.toString().trim()

            if (regPassword != regPassword2) {
                progress_layout.visibility = View.GONE
                showToast(this, getString(R.string.password_not_equal))
            } else {
                if (AuthenticationModel.validateUsername(user_reg_name) && (AuthenticationModel.validateEmail(user_reg_email) && AuthenticationModel.validatePassword(user_reg_password_first) && AuthenticationModel.validatePassword(user_reg_password_second))) {
                    regViewModel.createEthereumWallet()
                    regViewModel.createWallet.observe(this, Observer { ethAddress ->
                        println(ethAddress)
                        PrefUtils.saveEthereumAddress(ethAddress.toString())
                    })


                    regViewModel.bitcoinWallet()
                    regViewModel.createBitcoinWallet.observe(this, Observer { btcAddress ->
                        println(btcAddress)
                        PrefUtils.saveBtcAddress(btcAddress.toString())
                    })

                    regViewModel.ethPrivateKey.observe(this, Observer { ethPrivate ->
                        println(ethPrivate.toString())
                        PrefUtils.saveEthKey(ethPrivate.toString())

                    })
                    regViewModel.btcPrivateKey.observe(this, Observer { btcPrivate ->
                        println(btcPrivate)
                        PrefUtils.saveBitKey(btcPrivate.toString())

                    })


                   android.os.Handler().postDelayed({
                       regViewModel.emailPasswordRegistration(
                           this,
                           username,
                           regEmail,
                           regPassword,
                           PrefUtils.getEthKey(),
                           PrefUtils.getEtherAddress(),
                           PrefUtils.getBitKey(),
                           PrefUtils.getBitcoinAddress()
                       )
                   },3000)

                } else {
                    showToast(this, getString(R.string.register_failed))
                }
            }
            regViewModel.registrationResult.observe(this, Observer { message ->
                if(message == "Successful Registration"){
                showSuccessRegister()
                progress_layout.visibility = View.GONE}
                else{
                    progress_layout.visibility = View.GONE

                }
            })
        }
    }

    private fun showSuccessRegister() {
        val fm: FragmentManager = supportFragmentManager
        val registerSuccessDialog: RegisterSuccessDialog = RegisterSuccessDialog().newInstance("Success")
        registerSuccessDialog.isCancelable = false
        registerSuccessDialog.show(fm, "fragment_edit_name")
    }
    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, LoginActivity::class.java))
    }

}

