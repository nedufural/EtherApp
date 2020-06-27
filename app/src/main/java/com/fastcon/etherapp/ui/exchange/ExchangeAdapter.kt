package com.fastcon.etherapp.ui.exchange

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.fastcon.etherapp.R
import com.fastcon.etherapp.base.BaseRecycleViewAdapter
import com.fastcon.etherapp.base.BaseViewHolder
import com.fastcon.etherapp.base.ItemClickListener
import com.fastcon.etherapp.data.model.entity.ExchangeRateEntity
import com.fastcon.etherapp.util.Utility.exponentialNumToString

class ExchangeAdapter(private val itemClickListener: ItemClickListener<ExchangeRateEntity>) :
    BaseRecycleViewAdapter<ExchangeRateEntity>(itemClickListener) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ExchangeRateEntity> {
        val convertView =
            LayoutInflater.from(parent.context).inflate(R.layout.items_exchange, parent, false)

        return ExchangeViewHolder(convertView, itemClickListener = itemClickListener)
    }

    class ExchangeViewHolder(
        convertView: View,
        itemClickListener: ItemClickListener<ExchangeRateEntity>
    ) :
        BaseViewHolder<ExchangeRateEntity>(itemClickListener, convertView) {

        private var currencyId: TextView = convertView.findViewById(R.id.currency_id)
        private var currencySymbol: TextView = convertView.findViewById(R.id.currency_symbol)
        private var currencyRate: TextView = convertView.findViewById(R.id.currency_exchange_rate)
        private var symbol: TextView = convertView.findViewById(R.id.symbol)

        override fun bindingData(data: ExchangeRateEntity?) {
            /**
             *
             * we need to setData() here for the onItemsCLick to
             * @param data
             * receive the data on the Activity
             * */
            setData(data)

            val exRates = String.format("%.4f", data?.rateUsd?.toDouble()).toDouble()
            if (exRates.toString().contains("E")) {
                currencyRate.text = exponentialNumToString(exRates)
            } else {
                currencyRate.text = exRates.toString()
            }
            currencyId.text = data?.id?.toUpperCase()
            currencySymbol.text = " ${data?.symbol?.toUpperCase()}"
            if (data?.currencySymbol == "null") {
                symbol.text = data.symbol
            } else {
                symbol.text = data?.currencySymbol
            }

        }
    }

}