package com.fastcon.etherapp.ui.traded_volume

import android.app.Activity
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.facebook.FacebookSdk.getApplicationContext
import com.fastcon.etherapp.R
import com.fastcon.etherapp.base.BaseFragment
import com.fastcon.etherapp.base.ItemClickListener
import com.fastcon.etherapp.data.local.PrefUtils
import com.fastcon.etherapp.data.remote.entity.TradedVolumeEntity
import com.fastcon.etherapp.ui.traded_volume_details.TradedVolumeDetails
import com.fastcon.etherapp.util.Extensions
import kotlinx.android.synthetic.main.fragment_traded_history.*
import timber.log.Timber


class TradedVolumeFragment : BaseFragment(), ItemClickListener<TradedVolumeEntity> {
    private lateinit var adapter: TradedVolumeRecyclerViewAdapter
    private lateinit var tradedViewModel: TradedVolumeViewModel
    override fun getLayoutId(): Int = R.layout.fragment_traded_history
    override fun initData() {


        adapter = TradedVolumeRecyclerViewAdapter(this)
        recycler_traded_history.layoutManager = LinearLayoutManager(context)

        val dividerItemDecoration =
            DividerItemDecoration(
                recycler_traded_history.context,
                (recycler_traded_history.layoutManager as LinearLayoutManager).orientation
            )
        recycler_traded_history.addItemDecoration(dividerItemDecoration)
        recycler_traded_history.addOnScrollListener(
            setRecyclerViewScrollListener(
                requireActivity(),
                search_traded_history
            )
        )

        recycler_traded_history.adapter = adapter

        tradedViewModel = ViewModelProviders.of(this).get(TradedVolumeViewModel::class.java)


    }

    override fun initEvent() {
        tradedViewModel.getTradedHistory()
        tradedViewModel.tVolumeMutableLiveData.observe(this, Observer { tradedData ->
            //adapter.clearAll()
            PrefUtils.saveTradedList(tradedData)
            adapter.setData(tradedData)
            search_traded_history.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence,
                    i: Int,
                    i1: Int,
                    i2: Int
                ) {
                }

                override fun onTextChanged(
                    charSequence: CharSequence,
                    i: Int,
                    i1: Int,
                    i2: Int
                ) {
                }

                override fun afterTextChanged(editable: Editable) {
                    //after the change calling the method and passing the search input
                    filterList(editable.toString(), tradedData, adapter)
                }
            })
        })

        currency_delete.setOnClickListener(View.OnClickListener {
            search_traded_history.text.clear()
        })

    }

    fun filterList(
        text: String,
        names: ArrayList<TradedVolumeEntity>,
        adapter: TradedVolumeRecyclerViewAdapter
    ) {
        //new array list that will hold the filtered data
        val filteredList: ArrayList<TradedVolumeEntity> = ArrayList()

        //looping through existing elements
        for (s in names) {
            //if the existing elements contains the search input
            if (s.id?.toLowerCase()?.contains(text.toLowerCase())!!) {
                //adding the element to filtered list
                filteredList.add(s)
            }
        }

        //calling a method of the adapter class and passing the filtered list
        adapter.updateData(filteredList)
    }

    override fun onItemClick(data: TradedVolumeEntity?, position: Int, typeClick: Int) {
        Timber.i(data?.id.toString())
        val id = Intent(getApplicationContext(), TradedVolumeDetails::class.java)
        id.putExtra("id", data?.id)
        id.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        getApplicationContext().startActivity(id)
    }

    private fun setRecyclerViewScrollListener(
        activity: Activity,
        searchBox: EditText
    ): OnScrollListener {
        return object : OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                Extensions.hideKeyBoard(activity, searchBox)
            }
        }
    }
}

