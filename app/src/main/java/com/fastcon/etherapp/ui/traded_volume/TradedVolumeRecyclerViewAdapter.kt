package com.fastcon.etherapp.ui.traded_volume

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.facebook.FacebookSdk.getApplicationContext
import com.fastcon.etherapp.R
import com.fastcon.etherapp.base.BaseRecycleViewAdapter
import com.fastcon.etherapp.base.BaseViewHolder
import com.fastcon.etherapp.base.ItemClickListener
import com.fastcon.etherapp.data.remote.entity.TradedVolumeEntity
import java.util.*

class TradedVolumeRecyclerViewAdapter(private val itemClickListener: ItemClickListener<TradedVolumeEntity>) :
    BaseRecycleViewAdapter<TradedVolumeEntity>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<TradedVolumeEntity> {
        val convertView = LayoutInflater.from(parent.context).inflate(R.layout.item_traded_history, parent, false)
        return TradedVolumeViewHolder(convertView, itemClickListener = itemClickListener)
    }

    class TradedVolumeViewHolder(convertView: View, itemClickListener: ItemClickListener<TradedVolumeEntity>) :
        BaseViewHolder<TradedVolumeEntity>(itemClickListener, convertView) {
        var conView: View = convertView
        var tVolume: TextView = convertView.findViewById(R.id.traded_volume)
        var  tDate: TextView = convertView.findViewById(R.id.traded_time)
        var tChange24h: TextView = convertView.findViewById(R.id.traded_change_24h)
        var tCurrencyName: TextView = convertView.findViewById(R.id.traded_currency_name)

        override fun bindingData(data: TradedVolumeEntity?) {

            setData(data)
            tDate.text = Date().toString()
            val tVol = String.format("%,.4f", data?.volumeUsd24Hr?.toDouble())
            tVolume.text = "$ $tVol"
            val tChange = String.format("%.4f", data?.changePercent24Hr?.toDouble()).toDouble()
            if (tChange.toString().contains("-", false)) {
                tChange24h.setTextColor(
                    ContextCompat.getColor(
                        getApplicationContext(),
                        R.color.red
                    )
                )
            } else {
                tChange24h.setTextColor(
                    ContextCompat.getColor(
                        getApplicationContext(),
                        R.color.green
                    )
                )
            }
            tChange24h.text = "${tChange}%"
            tCurrencyName.text = data?.id?.toUpperCase()
        }


    }

}
