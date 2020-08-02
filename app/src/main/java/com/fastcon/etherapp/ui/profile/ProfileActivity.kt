package com.fastcon.etherapp.ui.profile

import android.content.Intent
import android.os.AsyncTask
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.fastcon.etherapp.R
import com.fastcon.etherapp.base.BaseActivity
import com.fastcon.etherapp.data.local.PrefUtils
import com.fastcon.etherapp.databinding.ActivityProfileBinding
import com.fastcon.etherapp.service.AuthManager
import com.fastcon.etherapp.ui.dialogs.ReceiveFundsFragment
import com.fastcon.etherapp.ui.dialogs.SendFundsFragment
import com.fastcon.etherapp.ui.transaction.btc_transactions.BitcoinTransactionActivity
import com.fastcon.etherapp.ui.transaction.ether_transactions.EthereumTransactionActivity
import com.fastcon.etherapp.util.LogOutTimerTask
import com.fastcon.etherapp.util.common.Commons.bitcoin_balance
import com.github.angads25.toggle.interfaces.OnToggledListener
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.toolbar_activity.*
import timber.log.Timber
import java.util.*


class ProfileActivity : BaseActivity<ActivityProfileBinding>() {

    private var isEnabled: Boolean = false
    private var timer: Timer? = null
    override fun getLayoutId(): Int = R.layout.activity_profile

    override fun initData() {

        profile_text.text = PrefUtils.getUserName()
        var to = intent.getStringExtra("to")
        // var from = intent.getStringExtra("from")
        var value = intent.getStringExtra("value")


        showToast(applicationContext, " $to $value ")

        dataBinding?.fundsHistory?.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, BitcoinTransactionActivity::class.java))
        })
        dataBinding?.etherLayoutTx?.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, EthereumTransactionActivity::class.java))
        })

        dataBinding?.authSwitch?.setOnToggledListener(OnToggledListener { _, isOn ->
            if (!isOn) {
                AuthManager.logout(applicationContext)

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


        val usdConversionViewModel: UsdConversionViewModel =
            ViewModelProviders.of(this).get(UsdConversionViewModel::class.java)



        usdConversionViewModel.getConversionRates("ethereum")
        usdConversionViewModel.conversionResult?.observe(this, Observer { rates ->
            println("ether ${rates.data.rateUsd}")
        })

        usdConversionViewModel.getConversionRates("bitcoin")
        usdConversionViewModel.conversionResult?.observe(this, Observer { rates ->
            println("bitcoin ${rates.data.rateUsd}")
        })



        AsyncTask.execute {
            profileViewModel.getETHBalance(PrefUtils.getEtherAddress())
        }
        AsyncTask.execute {
            profileViewModel.uploadUserToken()
        }
        profileViewModel.ethBalance?.observe(this, Observer { ethBalance ->
            dataBinding?.profileEthereumBalance?.text = "ETH  ${ethBalance.toString()}"
            println(ethBalance)
        })

        AsyncTask.execute {
            profileViewModel.getBitcoinBalance(bitcoin_balance + PrefUtils.getBitcoinAddress())
        }
        profileViewModel._bitcoinBalanceLiveData.observe(this, Observer { btcBalance ->
            dataBinding?.profileBitcoinBalance?.text = "BTC  ${btcBalance.toString()}"
            println(btcBalance)
        })

        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            PrefUtils.saveDeviceToken(it.token)
        }

        dataBinding?.fundsTx?.setOnClickListener { v ->

            showAddressDialog()
        }

        dataBinding?.sendFundButton?.setOnClickListener { v ->
            sendFundDialog()
        }


    }

    private fun showAddressDialog() {

        val fm: FragmentManager = supportFragmentManager
        val receiveFundsFragment: ReceiveFundsFragment =
            ReceiveFundsFragment().newInstance("Addresses")
        receiveFundsFragment.show(fm, "fragment_edit_name")
    }

    private fun sendFundDialog() {
        val fm: FragmentManager = supportFragmentManager
        val sendFundsFragment: SendFundsFragment = SendFundsFragment().newInstance("Send Funds")
        sendFundsFragment.show(fm, "fragment_send_funds")
    }

    override fun onPause() {
        super.onPause()
        timer = Timer()
        Timber.i("Invoking logout timer")
        val logoutTimeTask = LogOutTimerTask()
        timer!!.schedule(logoutTimeTask, 300000) //auto logout in 5 minutes
    }

    override fun onResume() {
        super.onResume()
        if (timer != null) {
            timer!!.cancel()
            Timber.i("cancel timer")
            timer = null
        }
    }

}
