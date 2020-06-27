package com.fastcon.etherapp.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.fastcon.etherapp.R
import com.fastcon.etherapp.base.BaseActivity
import com.fastcon.etherapp.base.ItemClickListener
import com.fastcon.etherapp.databinding.ActivityHomeBinding
import com.fastcon.etherapp.ui.profile.ProfileActivity
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.toolbar_activity.*


class HomeActivity : BaseActivity<ActivityHomeBinding>() {
    override fun getLayoutId(): Int = R.layout.activity_home
    override fun initData() {

        profile_text.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }

    override fun initEvent() {

    }

    var tabLayout: TabLayout? = null
    var viewPager: ViewPager? = null
    private fun get() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.main_board)


        tabLayout!!.addTab(tabLayout!!.newTab().setText(getString(R.string.rates)))
        tabLayout!!.addTab(tabLayout!!.newTab().setText(getString(R.string.transaction)))
        //tabLayout!!.addTab(tabLayout!!.newTab().setText(getString(R.string.transfer)))
        tabLayout!!.addTab(tabLayout!!.newTab().setText(getString(R.string.news)))

        val adapter = MyAdapter(this, supportFragmentManager, tabLayout!!.tabCount)
        viewPager!!.adapter = adapter
        viewPager!!.pageMargin = 7
        viewPager!!.setPageMarginDrawable(R.color.hash)

        viewPager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager!!.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })



    }

}
