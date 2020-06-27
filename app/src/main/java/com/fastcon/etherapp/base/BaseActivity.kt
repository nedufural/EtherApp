package com.fastcon.etherapp.base

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.fastcon.etherapp.R
import com.google.firebase.auth.FirebaseAuth


abstract class BaseActivity<B : ViewDataBinding> : AppCompatActivity() {

    var dataBinding: B? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindViewLayout()
        statusBarColourChanger()
        initData()
        initEvent()
        setUpToolbar()
    }


    abstract fun getLayoutId(): Int
    protected open fun bindViewLayout() {
        dataBinding = DataBindingUtil.setContentView(this, getLayoutId())
    }

    protected open fun initData() {}

    protected open fun initEvent() {}

    private fun statusBarColourChanger() {
        window.statusBarColor = resources.getColor(R.color.colorPrimary, theme)
    }

    fun showToast(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()

    }


    private fun setUpToolbar() {
        var toolbar = findViewById<Toolbar>(R.id.toolbar_activity)
        toolbar.navigationIcon = resources.getDrawable(R.drawable.ic_back)
        toolbar.setNavigationOnClickListener { onBackPressed() }

    }
}
