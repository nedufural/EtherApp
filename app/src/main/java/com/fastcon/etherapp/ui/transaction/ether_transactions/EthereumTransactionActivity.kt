package com.fastcon.etherapp.ui.transaction.ether_transactions

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.FacebookSdk
import com.fastcon.etherapp.R
import com.fastcon.etherapp.base.BaseActivity
import com.fastcon.etherapp.base.ItemClickListener
import com.fastcon.etherapp.data.local.PrefUtils
import com.fastcon.etherapp.data.remote.entity.EthereumTransactionEntity
import com.fastcon.etherapp.databinding.EthereumActivityBinding
import com.fastcon.etherapp.ui.transaction.TransferViewModel
import com.fastcon.etherapp.ui.transaction_details.EthereumTransactionDetails
import com.fastcon.etherapp.util.common.Commons
import kotlinx.android.synthetic.main.ethereum_activity.*
import kotlinx.android.synthetic.main.toolbar_activity.*


class EthereumTransactionActivity : BaseActivity<EthereumActivityBinding>(),
    ItemClickListener<EthereumTransactionEntity> {

    private var userAccount = "0xd8f4cef03adb10d5f5bd11d1e912c67a9f5221d6"
    private lateinit var adapter: EthereumTransactionAdapter
    private lateinit var transferViewModel: TransferViewModel

    override fun getLayoutId(): Int {
        return R.layout.ethereum_activity
    }


    override fun initData() {
        profile_text.text = PrefUtils.getUserName()
        adapter = EthereumTransactionAdapter(this)
        tx_list_eth.layoutManager = LinearLayoutManager(this)

        val dividerItemDecoration =
            DividerItemDecoration(
                tx_list_eth.context,
                (tx_list_eth.layoutManager as LinearLayoutManager).orientation
            )
        tx_list_eth.addItemDecoration(dividerItemDecoration)
        tx_list_eth.adapter = adapter
        transferViewModel = ViewModelProviders.of(this).get(TransferViewModel::class.java)
    }

    override fun initEvent() {

        transferViewModel.getEthereumTransactionHistory("${Commons.etherTransactionsUrl}$userAccount&startblock=0&endblock=99999999&sort=asc&apikey=${Commons.API_Key}")
        transferViewModel.getEthereumTransactionHistory()?.observe(this, Observer { list ->
            adapter.setData(list)

            clearSearchBox()
            searchList(list, search_eth_tx_list)
        })
    }

    private fun clearSearchBox() {
        currency_delete.setOnClickListener {
            search_eth_tx_list.setText("")
        }
    }

    private fun searchList(list: ArrayList<EthereumTransactionEntity>, editText: EditText) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
            }

            override fun onTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
            }

            override fun afterTextChanged(editable: Editable) {
                //after the change call the method and passing the search input
                filter(editable.toString(), list, adapter)
            }
        })
    }

    fun filter(
        text: String,
        names: ArrayList<EthereumTransactionEntity>,
        adapter: EthereumTransactionAdapter
    ) {
        //new array list that will hold the filtered data
        val filteredList: ArrayList<EthereumTransactionEntity> = ArrayList()

        //looping through existing elements
        for (s in names) {
            //if the existing elements contains the search input
            if (s.to.toLowerCase().contains(text.toLowerCase()) ||
                s.from.toLowerCase().contains(text.toLowerCase()) ||
                s.blockNumber.toLowerCase().contains(text.toLowerCase())
            ) {
                //adding the element to filtered list
                filteredList.add(s)
            }
        }

        //calling a method of the adapter class and passing the filtered list
        adapter.filterList(filteredList)


    }

    override fun onItemClick(data: EthereumTransactionEntity?, position: Int, typeClick: Int) {

        var intent =
            Intent(FacebookSdk.getApplicationContext(), EthereumTransactionDetails::class.java)
        intent.putExtra("to", data?.to)
        intent.putExtra("value", data?.value)
        intent.putExtra("timeStamp", data?.timeStamp)
        intent.putExtra("from", data?.from)
        intent.putExtra("txReceiptStatus", data?.txReceiptStatus)
        intent.putExtra("blockHash", data?.blockHash)
        intent.putExtra("blockNumber", data?.blockNumber)
        intent.putExtra("confirmations", data?.confirmations)
        intent.putExtra("gas", data?.gas)
        intent.putExtra("gasUsed", data?.gasUsed)
        intent.putExtra("gasPrice", data?.gasPrice)
        intent.putExtra("nonce", data?.nonce)
        intent.putExtra("transactionIndex", data?.transactionIndex)
        intent.putExtra("hash", data?.hash)
        startActivity(intent)
    }
}