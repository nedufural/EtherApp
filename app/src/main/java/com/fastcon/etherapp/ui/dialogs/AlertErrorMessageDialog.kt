package com.fastcon.etherapp.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fastcon.etherapp.R
import com.fastcon.etherapp.base.BaseDialog
import com.fastcon.etherapp.data.local.PrefUtils
import com.fastcon.etherapp.util.views.FragmentUtils
import kotlinx.android.synthetic.main.error_layout.*
import kotlinx.android.synthetic.main.fragment_receive_funds.*
import kotlinx.android.synthetic.main.fragment_receive_funds.send_fund_close_button
import kotlinx.android.synthetic.main.fragment_send_funds.*

class AlertErrorMessageDialog : BaseDialog() {
    override fun getLayoutId(): Int {
        return R.layout.error_layout
    }

    fun newInstance(title: String?): AlertErrorMessageDialog {
        val frag = AlertErrorMessageDialog()
        val args = Bundle()
        args.putString("title", title)
        frag.arguments = args
        return frag
    }


    override fun initData() {
        close.setOnClickListener { FragmentUtils.popBackStack(activity?.supportFragmentManager!!, this) }
        error_alert_msg.text = getString(R.string.no_transaction_found)
    }

}