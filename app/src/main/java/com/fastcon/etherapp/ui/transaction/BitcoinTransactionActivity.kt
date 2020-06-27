package com.fastcon.etherapp.ui.transaction

import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.fastcon.etherapp.R
import com.fastcon.etherapp.base.BaseActivity
import com.fastcon.etherapp.base.ItemClickListener
import com.fastcon.etherapp.data.remote.entity.BitcoinTXEntity
import com.fastcon.etherapp.databinding.BitcoinActivityBinding
import com.fastcon.etherapp.ui.transaction_details.BitcoinTransactionDetails
import com.fastcon.etherapp.util.Commons
import kotlinx.android.synthetic.main.bitcoin_activity.*


class BitcoinTransactionActivity : BaseActivity<BitcoinActivityBinding>(),
    ItemClickListener<BitcoinTXEntity> {

    private lateinit var adapterBtc: BitcoinTransactionAdapter
    private lateinit var transferViewModel: TransferViewModel

    override fun getLayoutId(): Int {
        return R.layout.bitcoin_activity
    }

    override fun initData() {


        adapterBtc = BitcoinTransactionAdapter(this)
        tx_list_btc.layoutManager = LinearLayoutManager(this)

        val dividerItemDecoration =
            DividerItemDecoration(
                tx_list_btc.context,
                (tx_list_btc.layoutManager as LinearLayoutManager).orientation
            )




        tx_list_btc.addItemDecoration(dividerItemDecoration)
        tx_list_btc.adapter = adapterBtc
        transferViewModel = ViewModelProviders.of(this).get(TransferViewModel::class.java)


    }

    override fun initEvent() {
        transferViewModel.getBitcoinTransactionHistory(Commons.btcTransactionsUrl)
        transferViewModel.bitcoinTxHistoryMutableLiveData.observe(this, Observer { list ->
            adapterBtc.setData(list)
        })
    }

    override fun onItemClick(data: BitcoinTXEntity?, position: Int, typeClick: Int) {

        val intent = Intent(this,BitcoinTransactionDetails::class.java)
        intent.putExtra("input",data?.inputs as Array<String>)
        intent.putExtra("output",data.out as Array<String>)
        startActivity(intent)
    }


}