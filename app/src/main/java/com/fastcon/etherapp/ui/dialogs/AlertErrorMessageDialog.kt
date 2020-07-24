package com.fastcon.etherapp.ui.dialogs

import android.os.Bundle
import com.fastcon.etherapp.R
import com.fastcon.etherapp.base.BaseDialog

class AlertErrorMessageDialog:BaseDialog() {
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
}