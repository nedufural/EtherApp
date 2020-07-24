package com.fastcon.etherapp.ui.dialogs

import android.os.Bundle
import com.fastcon.etherapp.R
import com.fastcon.etherapp.base.BaseDialog

class SendingFundsProgress : BaseDialog(){
    override fun getLayoutId(): Int {
        return R.layout.send_funds_progress
    }
    fun newInstance(title: String?): SendingFundsProgress {
        val frag = SendingFundsProgress()
        val args = Bundle()
        args.putString("fragment_send_funds", title)
        frag.arguments = args
        return frag
    }
}