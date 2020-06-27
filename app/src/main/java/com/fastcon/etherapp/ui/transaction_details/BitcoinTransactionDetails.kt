package com.fastcon.etherapp.ui.transaction_details

import com.fastcon.etherapp.R
import com.fastcon.etherapp.base.BaseActivity
import com.fastcon.etherapp.databinding.ActivityBitcoinTransactionDetailsBinding

class BitcoinTransactionDetails : BaseActivity<ActivityBitcoinTransactionDetailsBinding>() {
    override fun getLayoutId(): Int = R.layout.activity_bitcoin_transaction_details


    override fun initData() {
        super.initData()
        val intents = intent.getStringArrayExtra("input")
        println(intents[0])
    }

}
