package com.fastcon.etherapp.ui.profile

import android.content.Intent
import android.os.AsyncTask
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.fastcon.etherapp.R
import com.fastcon.etherapp.base.BaseActivity
import com.fastcon.etherapp.data.local.PrefUtils
import com.fastcon.etherapp.databinding.ActivityProfileBinding
import com.fastcon.etherapp.ui.login.LoginActivity
import com.fastcon.etherapp.ui.transaction.BitcoinTransactionActivity
import com.fastcon.etherapp.ui.transaction.EthereumTransactionActivity
import com.fastcon.etherapp.util.Commons.bitcoin_balance
import com.github.angads25.toggle.interfaces.OnToggledListener


class ProfileActivity : BaseActivity<ActivityProfileBinding>() {
    // private val profileBinding: ActivityProfileBinding = ActivityProfileBinding()
    override fun getLayoutId(): Int = R.layout.activity_profile
    override fun initData() {
        var to = intent.getStringExtra("to")
        // var from = intent.getStringExtra("from")
        var value = intent.getStringExtra("value")

        showToast(applicationContext, " $to $value ")

        dataBinding?.bitcoinTx?.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, BitcoinTransactionActivity::class.java))
        })
        dataBinding?.etherTx?.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, EthereumTransactionActivity::class.java))
        })

        dataBinding?.authSwitch?.setOnToggledListener(OnToggledListener { _, isOn ->
            if (!isOn) {
                PrefUtils.setSignedIn(false)
                startActivity(Intent(this, LoginActivity::class.java))
            }
        })
        dataBinding?.profileBitcoinAddress?.text = PrefUtils.getBitcoinAddress()
        dataBinding?.profileEthereumAddress?.text = PrefUtils.getEtherAddress()
        dataBinding?.profileUserNameEther?.text = PrefUtils.getUserName()
        dataBinding?.profileUserNameBtc?.text = PrefUtils.getUserName()
    }

    override fun initEvent() {
        val profileViewModel: ProfileViewModel =
            ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        AsyncTask.execute {
            profileViewModel.getETHBalance(PrefUtils.getEtherAddress())
        }
        profileViewModel.ethBalance?.observe(this, Observer { ethBalance ->
            dataBinding?.profileEthereumBalance?.text = ethBalance.toString()
            println(ethBalance)
        })

        AsyncTask.execute {
            profileViewModel.getBitcoinBalance(bitcoin_balance+PrefUtils.getBitcoinAddress())
        }
        profileViewModel._bitcoinBalanceLiveData.observe(this, Observer { btcBalance ->
            dataBinding?.profileBitcoinBalance?.text = btcBalance.toString()
            println(btcBalance)
        })
    }
}
