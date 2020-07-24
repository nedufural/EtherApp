package com.fastcon.etherapp.base

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.fastcon.etherapp.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.toolbar_activity.*


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
    fun showSnackBar(view: View, text: String) {
        Snackbar.make(view, text, Snackbar.LENGTH_LONG).show()
    }
    fun showSnackBarDuration(view: View, text: String) {
        Snackbar
            .make(view, text, 5000)
            .setActionTextColor(Color.MAGENTA)
            .show();
    }

    private fun setUpToolbar() {
        var toolbar = findViewById<Toolbar>(R.id.toolbar_activity)
        toolbar.navigationIcon = ContextCompat.getDrawable(applicationContext,R.drawable.ic_back)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }
}
