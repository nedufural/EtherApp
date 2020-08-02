package com.fastcon.etherapp.ui.dialogs

import android.os.Bundle
import com.fastcon.etherapp.R
import com.fastcon.etherapp.base.BaseDialog
import com.fastcon.etherapp.data.local.PrefUtils
import com.fastcon.etherapp.util.views.FragmentUtils
import kotlinx.android.synthetic.main.fragment_receive_funds.*
import kotlinx.android.synthetic.main.fragment_send_funds.send_fund_close_button


class ReceiveFundsFragment : BaseDialog() {


    fun newInstance(title: String?): ReceiveFundsFragment {
        val frag = ReceiveFundsFragment()
        val args = Bundle()
        args.putString("fragment_send_funds", title)
        frag.arguments = args
        return frag
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_receive_funds
    }

    override fun initData() {
        val bottomSheet = QrCodeAddressImage()
        receive_btc.setOnClickListener {
            FragmentUtils.popBackStack(activity?.supportFragmentManager!!, this)
            bottomSheet.show(activity?.supportFragmentManager!!, "exampleBottomSheet")
            PrefUtils.setCurrencyDisplayID(1);
        }
        receive_eth.setOnClickListener {
            FragmentUtils.popBackStack(activity?.supportFragmentManager!!, this)
            bottomSheet.show(activity?.supportFragmentManager!!, "exampleBottomSheet")
            PrefUtils.setCurrencyDisplayID(2);
        }
        send_fund_close_button.setOnClickListener {
            FragmentUtils.popBackStack(activity?.supportFragmentManager!!, this)


        }


    }
}