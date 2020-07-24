package com.fastcon.etherapp.ui.transaction_details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.fastcon.etherapp.R
import com.fastcon.etherapp.base.*
import com.fastcon.etherapp.data.remote.model.BitcoinResponse

class BitcoinTransactionDetailsOutputAdapter() : BaseRecyclerViewAdapterPlain<BitcoinResponse.Tx.Out>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolderPlain<BitcoinResponse.Tx.Out> {
        val convertView = LayoutInflater.from(parent.context).inflate(R.layout.items_btc_tnx_output, parent, false)
        return BitcoinTnxInputViewHolder(convertView)
    }

    class BitcoinTnxInputViewHolder(
        convertView: View
    ) :
        BaseViewHolderPlain<BitcoinResponse.Tx.Out>(convertView) {
        private var conView: View = convertView
        private var inputAddress: TextView = convertView.findViewById(R.id.btc_tnx_output_add)
        private var inputValue: TextView = convertView.findViewById(R.id.btc_tnx_output_val)


        override fun bindingData(data: BitcoinResponse.Tx.Out?) {
            setData(data)
            inputAddress.text = data?.addr
            inputValue.text = data?.value.toString()
        }

    }
}