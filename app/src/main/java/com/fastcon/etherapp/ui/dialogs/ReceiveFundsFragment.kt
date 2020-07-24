package com.fastcon.etherapp.ui.dialogs

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.fastcon.etherapp.R
import com.fastcon.etherapp.base.BaseDialog
import com.fastcon.etherapp.data.local.PrefUtils
import com.fastcon.etherapp.util.common.Commons


class ReceiveFundsFragment : BaseDialog() {

    private var mImageEthAddress: ImageView? = null
    private var mImageBtcAddress: ImageView? = null


    fun newInstance(title: String?): ReceiveFundsFragment {
        val frag = ReceiveFundsFragment()
        val args = Bundle()
        args.putString("title", title)
        frag.arguments = args
        return frag
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_receive_funds
    }

    override fun initData() {
        mImageBtcAddress = view?.findViewById<View>(R.id.btcImgAddress) as ImageView
        mImageEthAddress = view?.findViewById<View>(R.id.ethImgAddress) as ImageView
        mImageEthAddress!!.setImageBitmap(Commons.encodeAsBitmap(PrefUtils.getEtherAddress()))
        mImageBtcAddress!!.setImageBitmap(Commons.encodeAsBitmap(PrefUtils.getBitcoinAddress()))
    }
}