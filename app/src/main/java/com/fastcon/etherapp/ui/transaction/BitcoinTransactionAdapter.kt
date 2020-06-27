package com.fastcon.etherapp.ui.transaction

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.fastcon.etherapp.R
import com.fastcon.etherapp.base.BaseRecycleViewAdapter
import com.fastcon.etherapp.base.BaseViewHolder
import com.fastcon.etherapp.base.ItemClickListener
import com.fastcon.etherapp.data.remote.entity.BitcoinTXEntity
import com.fastcon.etherapp.util.Utility

class BitcoinTransactionAdapter(private val itemClickListener: ItemClickListener<BitcoinTXEntity>) :
    BaseRecycleViewAdapter<BitcoinTXEntity>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<BitcoinTXEntity> {
        val convertView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_bitcoin_tx, parent, false)

        return BitcoinTxViewHolder(convertView, itemClickListener = itemClickListener)
    }

    class BitcoinTxViewHolder(
        convertView: View,
        itemClickListener: ItemClickListener<BitcoinTXEntity>
    ) :
        BaseViewHolder<BitcoinTXEntity>(itemClickListener, convertView) {
        var hash: TextView = convertView.findViewById(R.id.hash)
        var time: TextView = convertView.findViewById(R.id.time)


        override fun bindingData(data: BitcoinTXEntity?) {

            setData(data)
            hash.text = data?.hash
            time.text = Utility.epochToDateFormatter(data?.time?.toLong())

        }

    }
}