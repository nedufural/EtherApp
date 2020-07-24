package com.fastcon.etherapp.ui.traded_volume_details

import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.fastcon.etherapp.R
import com.fastcon.etherapp.base.BaseActivity
import com.fastcon.etherapp.data.local.PrefUtils
import com.fastcon.etherapp.data.local.PrefUtils.getTradedHistoryDetailTime
import com.fastcon.etherapp.databinding.ActivityTransactionDetailsBinding
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.android.synthetic.main.activity_traded_volume_details.*
import kotlinx.android.synthetic.main.toolbar_activity.*
import timber.log.Timber
import java.util.*


class TradedVolumeDetails : BaseActivity<ActivityTransactionDetailsBinding>() {

    private lateinit var tradedDetailsViewModel: TradedDetailsViewModel
    private lateinit var mChart: GraphView
    private lateinit var series: LineGraphSeries<DataPoint>
    override fun getLayoutId(): Int {
        return R.layout.activity_traded_volume_details
    }

    override fun initData() {
        profile_text.text = PrefUtils.getUserName()



        tradedDetailsViewModel = ViewModelProviders.of(this).get(TradedDetailsViewModel::class.java)
        val id = intent.getStringExtra("id")
        if (id == null) {
            Timber.i("Error opening details")
        } else {

            tradedDetailsViewModel.getTradedDetails(id)
        }
    }


    override fun initEvent() {
        progress_layout.visibility = View.VISIBLE
        tradedDetailsViewModel.tVolumeDetailMutableLiveData.observe(this, Observer { response ->
            progress_layout.visibility = View.GONE
            t_detail_change.text = String.format("%.4f",response.changePercent24Hr?.toDouble())
            t_detail_market_cap.text = String.format("%,.4f", response?.marketCapUsd?.toDouble())
            if(response.maxSupply == null){
                t_detail_max_supply.text = "Details not available"
            }
            else{
            t_detail_max_supply.text = String.format("%,.4f",response.maxSupply.toDouble())}
            t_detail_name.text = response.name.toString()
            t_detail_price.text = String.format("%,.4f",response.priceUsd?.toDouble())
            t_detail_rank.text = response.rank.toString()
            t_detail_supply.text = String.format("%,.4f",response.supply?.toDouble())
            t_detail_symbol.text = response.symbol.toString()
            t_detail_volume.text = String.format("%,.4f",response.volumeUsd24Hr?.toDouble())
            t_detail_vwap.text = String.format("%,.4f",response.vwap24Hr?.toDouble())
            val time: Long = getTradedHistoryDetailTime();
            t_detail_timestamp.text = Date().toString()
        })


    }


}

