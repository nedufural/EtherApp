package com.fastcon.etherapp.ui.home

import android.Manifest
import android.content.Intent
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.fastcon.etherapp.R
import com.fastcon.etherapp.base.BaseActivity
import com.fastcon.etherapp.data.local.PrefUtils
import com.fastcon.etherapp.data.remote.entity.User
import com.fastcon.etherapp.databinding.ActivityHomeBinding
import com.fastcon.etherapp.ui.profile.ProfileActivity
import com.fastcon.etherapp.util.common.Commons.removeSpecialCharacter
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.toolbar_activity.*
import timber.log.Timber


class HomeActivity : BaseActivity<ActivityHomeBinding>() {
    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null

    private lateinit var myRef: DatabaseReference
    private lateinit var user: FirebaseUser
    private lateinit var homeViewModel:HomeViewModel

    override fun getLayoutId(): Int {
        return R.layout.activity_home
    }

    override fun initData() {
        initTabViewPager()
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        goToProfile(profile_text)
    }

    override fun initEvent() {
        initAuth()
        getUserCredentials()
        getDBError()
        checkResourcePermissions()
    }

    private fun goToProfile(text:TextView) {
        text.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }



    private fun checkResourcePermissions() {
        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest?>?,
                    token: PermissionToken?
                ) {
                }
            }).check()
    }


    private fun initAuth() {
        user = FirebaseAuth.getInstance().currentUser!!
        val database = FirebaseDatabase.getInstance()
        myRef = database.getReferenceFromUrl("https://etherapp-15902.firebaseio.com/Users").child(removeSpecialCharacter(PrefUtils.getEmail()))
        homeViewModel.retrieveUserDetails(myRef)
    }
    private fun getUserCredentials() {
        homeViewModel.getUserCredentials()?.observe(this, Observer { user ->
            if (user == null) {
                showToast()
            } else {
                saveUserCredentials(user)
            }
        })
    }


    private fun getDBError() {
        homeViewModel.getDatabaseErrorRetrieving()?.observe(this, Observer { message ->
            Timber.i(message)
            Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
        })
    }



    private fun showToast() {
        Toast.makeText(
            applicationContext,
            getString(R.string.user_unavaliable),
            Toast.LENGTH_LONG
        )
            .show()
    }

    private fun saveUserCredentials(user: User) {
        val etherAddress: String = user.pubKeyEther.toString()
        val btcAddress: String = user.pubKeyBTC.toString()
        val userName: String = user.name.toString()
        val privateBtc: String = user.privKeyBTC.toString()
        val privateEth: String = user.privKeyEther.toString()
        profile_text.text = userName
        PrefUtils.saveBitKey(privateBtc)
        PrefUtils.saveEthKey(privateEth)
        PrefUtils.saveUserName(userName)
        PrefUtils.saveBtcAddress(btcAddress)
        PrefUtils.saveEthereumAddress(etherAddress)
    }

    private fun initTabViewPager() {
        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.main_board)


        tabLayout!!.addTab(tabLayout!!.newTab().setText(getString(R.string.rates)))
        tabLayout!!.addTab(tabLayout!!.newTab().setText(getString(R.string.transaction)))
        tabLayout!!.addTab(tabLayout!!.newTab().setText(getString(R.string.news)))

        val adapter = MyAdapter(supportFragmentManager, tabLayout!!.tabCount)
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
    //splash screen the right way
    //https://medium.com/@ssaurel/create-a-splash-screen-on-android-the-right-way-93d6fb444857
    //https://medium.com/koderlabs/viewmodel-with-viewmodelprovider-factory-the-creator-of-viewmodel-8fabfec1aa4f
}
