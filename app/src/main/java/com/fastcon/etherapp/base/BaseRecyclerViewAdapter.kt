package com.fastcon.etherapp.base

import androidx.recyclerview.widget.RecyclerView


abstract class BaseRecycleViewAdapter<T : Any>(var clickListener: ItemClickListener<T>? = null) :
    RecyclerView.Adapter<BaseViewHolder<T>>() {
    private var mListData: ArrayList<T> = arrayListOf()

    override fun getItemCount(): Int {
        return mListData.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun setData(list: List<T>) {

        mListData = list as ArrayList<T>
        notifyDataSetChanged()
    }

     fun filterList(filteredNames: ArrayList<T>) {
        mListData = filteredNames
        notifyDataSetChanged()
    }

    fun addData(t: T) {
        mListData.add(t)

        notifyDataSetChanged()
    }

    fun getData(): ArrayList<T> {
        return mListData
    }

    fun clearAll() {
        mListData.clear()
        notifyDataSetChanged()
    }

    fun updateData(list: List<T>) {
        mListData = list as ArrayList<T>
        notifyDataSetChanged()

    }

    fun remove(position: Int) {
        mListData.removeAt(position)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        holder.bindingData(mListData[position])
    }



     fun restoreItem(item: T, position: Int) {
        mListData.add(position, item)
        notifyItemInserted(position)
    }

}