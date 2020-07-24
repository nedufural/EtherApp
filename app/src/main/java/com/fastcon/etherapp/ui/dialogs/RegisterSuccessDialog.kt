package com.fastcon.etherapp.ui.dialogs

import android.content.Intent
import android.os.Bundle
import com.fastcon.etherapp.R
import com.fastcon.etherapp.base.BaseDialog
import com.fastcon.etherapp.ui.login.LoginActivity
import kotlinx.android.synthetic.main.register_success_dialog.*

class RegisterSuccessDialog :BaseDialog(){
    override fun getLayoutId(): Int {
        return R.layout.register_success_dialog
    }



    fun newInstance(title: String?): RegisterSuccessDialog {
        val frag = RegisterSuccessDialog()
        val args = Bundle()
        args.putString("title", title)
        frag.arguments = args
        return frag
    }


    override fun initData() {

        mLoginButton.setOnClickListener {
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
        }
    }

}