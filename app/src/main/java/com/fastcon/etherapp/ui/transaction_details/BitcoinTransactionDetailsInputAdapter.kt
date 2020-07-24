package com.fastcon.etherapp.ui.transaction_details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.fastcon.etherapp.R
import com.fastcon.etherapp.base.BaseRecycleViewAdapter
import com.fastcon.etherapp.base.BaseRecyclerViewAdapterPlain
import com.fastcon.etherapp.base.BaseViewHolderPlain
import com.fastcon.etherapp.data.remote.model.BitcoinResponse

class BitcoinTransactionDetailsInputAdapter() :
    BaseRecyclerViewAdapterPlain<BitcoinResponse.Tx.Input>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolderPlain<BitcoinResponse.Tx.Input> {
        val convertView =
            LayoutInflater.from(parent.context).inflate(R.layout.items_btc_tnx_input, parent, false)
        return BitcoinTnxInputViewHolder(convertView)
    }

    class BitcoinTnxInputViewHolder(
        convertView: View
    ) :
        BaseViewHolderPlain<BitcoinResponse.Tx.Input>(convertView) {
        private var conView: View = convertView
        private var inputAddress: TextView = convertView.findViewById(R.id.btc_tnx_input_add)
        private var inputValue: TextView = convertView.findViewById(R.id.btc_tnx_input_val)


        override fun bindingData(data: BitcoinResponse.Tx.Input?) {
            setData(data)
            inputAddress.text = data?.prevOut?.addr.toString()
            inputValue.text = data?.prevOut?.value.toString()

        }

    }
}