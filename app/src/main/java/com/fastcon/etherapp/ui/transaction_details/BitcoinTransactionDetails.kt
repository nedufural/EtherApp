package com.fastcon.etherapp.ui.transaction_details

import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration

import androidx.recyclerview.widget.LinearLayoutManager
import com.fastcon.etherapp.R
import com.fastcon.etherapp.base.BaseActivity
import com.fastcon.etherapp.base.ItemClickListener
import com.fastcon.etherapp.data.local.PrefUtils

import com.fastcon.etherapp.data.remote.model.BitcoinResponse
import com.fastcon.etherapp.databinding.ActivityBitcoinTransactionDetailsBinding
import kotlinx.android.synthetic.main.activity_bitcoin_transaction_details.*

import kotlinx.android.synthetic.main.fragment_news.*
import kotlinx.android.synthetic.main.toolbar_activity.*

class BitcoinTransactionDetails : BaseActivity<ActivityBitcoinTransactionDetailsBinding>() {

    private lateinit var adapterInput: BitcoinTransactionDetailsInputAdapter
    private lateinit var adapterOutput: BitcoinTransactionDetailsOutputAdapter

    override fun getLayoutId(): Int {
        return R.layout.activity_bitcoin_transaction_details
    }


    override fun initData() {
        profile_text.text = PrefUtils.getUserName()
        val bitInEntity = PrefUtils.getBitcoinInputDetails()
        val bitOutEntity = PrefUtils.getBitcoinOutputDetails()
        if (bitInEntity == null) {
            showToast(this, "Input data is Empty")
        }
        if (bitOutEntity == null) {
            showToast(this, "Output data is Empty")
        } else {
            adapterOutput = BitcoinTransactionDetailsOutputAdapter()
            output_btc_detail.layoutManager = LinearLayoutManager(this)

            val dividerItemDecoration1 =
                DividerItemDecoration(
                    output_btc_detail.context,
                    (output_btc_detail.layoutManager as LinearLayoutManager).orientation
                )

            output_btc_detail.addItemDecoration(dividerItemDecoration1)
            output_btc_detail.adapter = adapterOutput


            adapterOutput.setData(bitOutEntity)







            adapterInput = BitcoinTransactionDetailsInputAdapter()
            input_btc_detail.layoutManager = LinearLayoutManager(this)

            val dividerItemDecoration = DividerItemDecoration(
                input_btc_detail.context,
                (input_btc_detail.layoutManager as LinearLayoutManager).orientation
            )

            input_btc_detail.addItemDecoration(dividerItemDecoration)
            input_btc_detail.adapter = adapterInput


            adapterInput.setData(bitInEntity)

        }
    }

}
