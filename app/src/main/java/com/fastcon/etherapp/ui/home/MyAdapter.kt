package com.fastcon.etherapp.ui.home

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.fastcon.etherapp.ui.news.NewsFragment
import com.fastcon.etherapp.ui.traded_volume.TradedVolumeFragment
import com.fastcon.etherapp.ui.exchange.ExchangeFragment
//import com.fastcon.etherapp.ui.transfer.TransferFragment

class MyAdapter(private val myContext: Context, fm: FragmentManager, private var totalTabs: Int) : FragmentPagerAdapter(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    // this is for fragment tabs
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                ExchangeFragment()
            }
            1 -> {
                TradedVolumeFragment()
            }
            2 -> {
                NewsFragment()
            }
            else -> ExchangeFragment()
        }
    }

    // this counts total number of tabs
    override fun getCount(): Int {
        return totalTabs
    }
}