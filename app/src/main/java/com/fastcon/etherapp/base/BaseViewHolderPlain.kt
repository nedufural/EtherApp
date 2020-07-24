package com.fastcon.etherapp.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolderPlain<T : Any>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var mData: T? = null

    abstract fun bindingData(data: T?)

    fun setData(data: T?) {
        mData = data
    }


}
