package com.fastcon.etherapp.ui.exchange


import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.fastcon.etherapp.R
import com.fastcon.etherapp.base.BaseFragment
import com.fastcon.etherapp.base.ItemClickListener
import com.fastcon.etherapp.data.model.entity.ExchangeRateEntity
import com.fastcon.etherapp.util.functions.KeyBoardUtils.Companion.editTextHideKeyBoard
import com.fastcon.etherapp.util.functions.NetworkUtil.Companion.checkInternet
import com.fastcon.etherapp.util.functions.OperationsUtils.exchangeRateReverseCalculation

import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_exchange.*
import java.util.*
import kotlin.collections.ArrayList


class ExchangeFragment : BaseFragment(), ItemClickListener<ExchangeRateEntity> {
    private lateinit var adapter: ExchangeAdapter
    private lateinit var exchangeViewModel: ExchangeViewModel


    override fun getLayoutId(): Int {
        return R.layout.fragment_exchange
    }

    override fun initData() {
        exchangeViewModel = ViewModelProviders.of(this).get(ExchangeViewModel::class.java)
        activity?.let { editTextHideKeyBoard(it, search_currencies) }
        adapter = ExchangeAdapter(this)
        exchange_list.layoutManager = LinearLayoutManager(context)

        recyclerViewDivideDecoration()
        adapter.setHasStableIds(true)
        exchange_list.adapter = adapter

    }
    override fun initEvent() {
        showCustomProgress()
        getExchangeRatesResponse()
        clearEditText(currency_delete)
        checkNetworkConnection()
        dbErrorMessage()
    }

    private fun dbErrorMessage() {
        exchangeViewModel.getExchangeErrorMsg()
            ?.observe(viewLifecycleOwner, Observer { errorResponse ->
                context?.let { showToast(it, errorResponse) }
            })
    }

    private fun checkNetworkConnection() {
        if (!checkInternet(context!!)) {
            Snackbar.make(requireView(), R.string.check_network, Snackbar.LENGTH_LONG).show()
            Toast.makeText(requireContext(), R.string.check_network, Toast.LENGTH_LONG).show()
        }
    }

    private fun getExchangeRatesResponse() {
        exchangeViewModel.getExchangeRates()
        exchangeViewModel.getExchangeRateMutableLiveData()
            ?.observe(viewLifecycleOwner, Observer { exchangeRates ->
                adapter.setData(exchangeRates)
                hideCustomProgress()
                searchCurrency(exchangeRates, search_currencies)
            })
    }

    private fun recyclerViewDivideDecoration() {
        val dividerItemDecoration =
            DividerItemDecoration(
                exchange_list.context,
                (exchange_list.layoutManager as LinearLayoutManager).orientation
            )
        exchange_list.addItemDecoration(dividerItemDecoration)
        exchange_list.addOnScrollListener(
            setRecyclerViewScrollListener(
                requireActivity(),
                search_currencies
            )
        )
    }

    private fun showCustomProgress() {
        progress_layout.visibility = View.VISIBLE
    }

    private fun hideCustomProgress() {
        progress_layout.visibility = View.GONE
    }

    private fun clearEditText(button: Button) {
        button.setOnClickListener {
            search_currencies.text.clear()
        }
    }

    private fun searchCurrency(data: ArrayList<ExchangeRateEntity>,searchBox: EditText) {
        searchBox.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun afterTextChanged(editable: Editable) {
                filterExchange(editable.toString(), data, adapter)
            }
        })
    }

    override fun onItemClick(data: ExchangeRateEntity?, position: Int, typeClick: Int) {
        val exchange = String.format("%.4f", data?.rateUsd?.toDouble()).toDouble()
        MaterialDialog(requireContext()).show {
            if (data?.currencySymbol == "null") {
                title(text = " USD vs ${data.symbol.toUpperCase()}")
            } else {
                title(text = " USD vs ${data?.currencySymbol?.toUpperCase()}")
            }
            message(
                text = "${data?.symbol}  ${String.format(
                            "%.6f",
                            exchangeRateReverseCalculation(exchange)
                        )} = 1 USD"
            )
        }
    }

    fun filterExchange(text: String, names: ArrayList<ExchangeRateEntity>, adapter: ExchangeAdapter) {
        val filteredList: ArrayList<ExchangeRateEntity> = ArrayList()
        for (s in names) {
            if (s.id.toLowerCase(Locale.getDefault()).contains(text.toLowerCase(Locale.getDefault()))
                || s.symbol.toLowerCase(Locale.getDefault()).contains(text.toLowerCase(Locale.getDefault()))) {
                filteredList.add(s)
            }
        }
        adapter.filterList(filteredList)
    }

    private fun setRecyclerViewScrollListener(activity: Activity, searchBox: EditText): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
               editTextHideKeyBoard(activity, searchBox)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
        exchange_list.adapter = adapter
    }
}