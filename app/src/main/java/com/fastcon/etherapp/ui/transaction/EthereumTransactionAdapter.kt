package com.fastcon.etherapp.ui.transaction

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.fastcon.etherapp.R
import com.fastcon.etherapp.base.BaseRecycleViewAdapter
import com.fastcon.etherapp.base.BaseViewHolder
import com.fastcon.etherapp.base.ItemClickListener
import com.fastcon.etherapp.data.remote.entity.EthereumTransactionEntity
import com.fastcon.etherapp.util.Utility.epochToDateFormatter

class EthereumTransactionAdapter(private val itemClickListener: ItemClickListener<EthereumTransactionEntity>) :
    BaseRecycleViewAdapter<EthereumTransactionEntity>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<EthereumTransactionEntity> {
        val convertView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_eth_tx, parent, false)

        return TransferViewHolder(convertView, itemClickListener = itemClickListener)
    }

    class TransferViewHolder(
        convertView: View,
        itemClickListener: ItemClickListener<EthereumTransactionEntity>
    ) :
        BaseViewHolder<EthereumTransactionEntity>(itemClickListener, convertView) {
        var conView: View = convertView
        var from: TextView = convertView.findViewById(R.id.from)
        var to: TextView = convertView.findViewById(R.id.to)
        var value: TextView = convertView.findViewById(R.id.value)
        var time: TextView = convertView.findViewById(R.id.time)


        override fun bindingData(data: EthereumTransactionEntity?) {

            setData(data)
            from.text = data?.from
            to.text = data?.to
            time.text = epochToDateFormatter(data?.timeStamp?.toLong())
            value.text = data?.value

        }

    }
}