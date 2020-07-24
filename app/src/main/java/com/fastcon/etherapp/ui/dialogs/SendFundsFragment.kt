package com.fastcon.etherapp.ui.dialogs


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.facebook.FacebookSdk.getApplicationContext
import com.fastcon.etherapp.R
import com.fastcon.etherapp.base.BaseDialog
import com.fastcon.etherapp.ui.send_funds.ethereum.SendEther
import com.fastcon.etherapp.util.views.FragmentUtils.Companion.popBackStack
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_send_funds.*


class SendFundsFragment : BaseDialog(){


    fun newInstance(title: String?): SendFundsFragment {
        val frag = SendFundsFragment()
        val args = Bundle()
        args.putString("fragment_send_funds", title)
        frag.arguments = args
        return frag
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_send_funds
    }

    override fun initData() {
        send_btc.setOnClickListener {
           /* startActivity(Intent(context,
            SendBitcoin::class.java))*/
            popBackStack(activity?.supportFragmentManager!!,this)

            Toasty.custom(getApplicationContext(), "Bitcoin transfer not supported now.",
                R.drawable.ic_baseline_warning_24, R.color.red, 10, true, true).show()
        }
        send_eth.setOnClickListener { startActivity(Intent(context,
            SendEther::class.java)) }
        send_fund_close_button.setOnClickListener {
            popBackStack(activity?.supportFragmentManager!!,this)
             }
    }
}