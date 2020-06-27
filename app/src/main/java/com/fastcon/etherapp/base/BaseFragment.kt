package com.fastcon.etherapp.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment


abstract class BaseFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()
        initEvent()
     
    }

    abstract fun getLayoutId(): Int

    protected open fun initData() {}

    protected open fun initEvent() {}

    fun showToast(activity: Context, msg: String) {
        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show()
    }

    override fun onStart() {
        super.onStart()
        println("_onStart")
    }

    override fun onStop() {
        super.onStop()
        println("_onStop")
    }

    override fun onResume() {
        super.onResume()
        println("_onResume")
    }

    override fun onDestroy() {
        super.onDestroy()
        println("_onDestroy")
    }

}