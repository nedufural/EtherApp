package com.fastcon.etherapp.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView


abstract class BaseViewHolder<T : Any>(
    itemClickListener: ItemClickListener<T>? = null,
    itemView: View
) : RecyclerView.ViewHolder(itemView) {
    var clicked = 0
    var clickListener: ItemClickListener<T>? = itemClickListener

    private var mData: T? = null

    abstract fun bindingData(data: T?)

    fun setData(data: T?) {
        mData = data
    }

    init {
        itemView.setOnClickListener {
            clickListener?.onItemClick(mData, adapterPosition, 0)


        }

    }

}

interface ItemClickListener<T> {
    fun onItemClick(data: T?, position: Int, typeClick: Int = 0)
}

