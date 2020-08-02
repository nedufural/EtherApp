package com.fastcon.etherapp.ui.transaction.btc_transactions

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.fastcon.etherapp.R
import com.fastcon.etherapp.base.BaseActivity
import com.fastcon.etherapp.base.ItemClickListener
import com.fastcon.etherapp.data.local.PrefUtils
import com.fastcon.etherapp.data.remote.entity.BitcoinTXEntity
import com.fastcon.etherapp.databinding.BitcoinActivityBinding
import com.fastcon.etherapp.ui.dialogs.AlertErrorMessageDialog
import com.fastcon.etherapp.ui.dialogs.RegisterSuccessDialog
import com.fastcon.etherapp.ui.transaction.TransferViewModel
import com.fastcon.etherapp.ui.transaction_details.BitcoinTransactionDetails
import com.fastcon.etherapp.util.common.Commons
import kotlinx.android.synthetic.main.bitcoin_activity.*
import kotlinx.android.synthetic.main.bitcoin_activity.currency_delete
import kotlinx.android.synthetic.main.toolbar_activity.*


class BitcoinTransactionActivity : BaseActivity<BitcoinActivityBinding>(),
    ItemClickListener<BitcoinTXEntity> {

    private lateinit var adapterBtc: BitcoinTransactionAdapter
    private lateinit var transferViewModel: TransferViewModel

    override fun getLayoutId(): Int {
        return R.layout.bitcoin_activity
    }

    override fun initData() {

        profile_text.text = PrefUtils.getUserName()
        adapterBtc =
            BitcoinTransactionAdapter(this)
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
        transferViewModel.getBitcoinTransactionHistory(Commons.btcTransactionsUrl + PrefUtils.getBitcoinAddress() + "?format=json")
        transferViewModel.getBitcoinTxHistoryMutableLiveData()?.observe(this, Observer { list ->
            adapterBtc.setData(list)
            clearSearchBox()
            searchBTCTransaction(list)
        })
        transferViewModel.getExchangeErrorMsg()?.observe(this, Observer { _ ->
            showNoTransactionMsg()
        })
    }

    private fun clearSearchBox() {
        currency_delete.setOnClickListener {
            search_btc_tx_list.setText("")
        }
    }

    private fun showNoTransactionMsg() {
        val fm: FragmentManager = supportFragmentManager
        val alertErrorMessageDialog: AlertErrorMessageDialog =
            AlertErrorMessageDialog().newInstance("Failed")
        alertErrorMessageDialog.isCancelable = true
        alertErrorMessageDialog.show(fm, "fragment_edit_name")
    }

    private fun searchBTCTransaction(list: ArrayList<BitcoinTXEntity>) {
        search_btc_tx_list.addTextChangedListener(object : TextWatcher {
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
                filter(editable.toString(), list, adapterBtc)
            }
        })
    }

    fun filter(
        text: String,
        names: ArrayList<BitcoinTXEntity>,
        adapter: BitcoinTransactionAdapter
    ) {
        //new array list that will hold the filtered data
        val filteredList: ArrayList<BitcoinTXEntity> = ArrayList()

        //looping through existing elements
        for (s in names) {
            //if the existing elements contains the search input
            if (s.hash.toLowerCase().contains(text.toLowerCase()) ||
                s.time.contains(text.toLowerCase())
            ) {
                //adding the element to filtered list
                filteredList.add(s)
            }
        }

        //calling a method of the adapter class and passing the filtered list
        adapter.filterList(filteredList)


    }

    override fun onItemClick(data: BitcoinTXEntity?, position: Int, typeClick: Int) {

        val intent = Intent(this, BitcoinTransactionDetails::class.java)
        PrefUtils.saveBitcoinInputDetails(data?.inputs!!)
        PrefUtils.saveBitcoinOutputDetails(data.out)
        startActivity(intent)
    }


}


