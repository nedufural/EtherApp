package com.fastcon.etherapp.ui.transaction_details


import com.fastcon.etherapp.R
import com.fastcon.etherapp.base.BaseActivity
import com.fastcon.etherapp.data.local.PrefUtils
import com.fastcon.etherapp.databinding.ActivityTransactionDetailsBinding
import kotlinx.android.synthetic.main.activity_transaction_details.*
import kotlinx.android.synthetic.main.toolbar_activity.*


class EthereumTransactionDetails : BaseActivity<ActivityTransactionDetailsBinding>() {


    override fun getLayoutId(): Int {
        return R.layout.activity_transaction_details
    }

    override fun initData() {
        profile_text.text = PrefUtils.getUserName()
        blockNumber.text = intent.getStringExtra("blockNumber")
        hash.text = intent.getStringExtra("hash")
        nonce.text = intent.getStringExtra("nonce")
        blockHash.text = intent.getStringExtra("blockHash")
        transactionIndex.text = intent.getStringExtra("transactionIndex")
        from.text = intent.getStringExtra("from")
        to.text = intent.getStringExtra("to")
        value.text = intent.getStringExtra("value")
        gas.text = intent.getStringExtra("gas")
        gasPrice.text = intent.getStringExtra("gasPrice")
        isError.text = intent.getStringExtra("isError")
        txreceipt_status.text = intent.getStringExtra("txReceiptStatus")
        input.text = intent.getStringExtra("input")
        contractAddress.text = intent.getStringExtra("contractAddress")
        cumulativeGasUsed.text = intent.getStringExtra("cumulativeGasUsed")
        gasUsed.text = intent.getStringExtra("timeStamp")
        confirmations.text = intent.getStringExtra("confirmations")
    }
}
