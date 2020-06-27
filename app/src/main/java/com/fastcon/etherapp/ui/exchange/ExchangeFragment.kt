package com.fastcon.etherapp.ui.exchange


import android.app.Activity
import android.graphics.Typeface
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
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
import com.fastcon.etherapp.util.Extensions
import com.fastcon.etherapp.util.Utility
import com.fastcon.etherapp.util.Utility.exchangeRateReverseCalculation
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_exchange.*
import timber.log.Timber


class ExchangeFragment : BaseFragment(), ItemClickListener<ExchangeRateEntity> {
    private lateinit var adapter: ExchangeAdapter
    private lateinit var exchangeViewModel: ExchangeViewModel


    override fun getLayoutId(): Int {
        return R.layout.fragment_exchange
    }

    override fun initData() {

        activity?.let { Extensions.hideKeyBoard(it, search_currencies) }
        adapter = ExchangeAdapter(this)
        exchange_list.layoutManager = LinearLayoutManager(context)

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
        adapter.setHasStableIds(true)
        exchange_list.adapter = adapter


        exchangeViewModel = ViewModelProviders.of(this).get(ExchangeViewModel::class.java)

    }

    override fun initEvent() {
        exchangeViewModel.getExchangeRates()
        exchangeViewModel.exchangeRateMutableLiveData.observe(viewLifecycleOwner, Observer { it ->

            adapter.setData(it)

            search_currencies.addTextChangedListener(object : TextWatcher {
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
                    //after the change call the method and passing the search input
                    filter(editable.toString(), it, adapter)
                }
            })
        })

        currency_delete.setOnClickListener(View.OnClickListener {
            search_currencies.text.clear()
        })

        /*if(activity?.currentFocus == search_currencies){
            searchBtn.visibility = View.GONE
        }*/

    /*    searchBtn.setOnClickListener(View.OnClickListener {
            search_currencies.visibility = View.VISIBLE
            currency_delete.visibility = View.VISIBLE
            searchBtn.visibility = View.GONE
            //Utility.slideFromRightToLeft(search_currencies)
        })*/
        /* val profileUpdates = UserProfileChangeRequest.Builder()
             .setDisplayName("Jane Q. User")
             .setPhotoUri(Uri.parse("https://daihoc.fpt.edu.vn/media/2017/01/sv-que1bb91c-te1babf-ce1bba7a-c491h-fpt-trc3acnh-bc3a0y-bc3a1o-cc3a1o-te1baa1i-e28098e1bba9ng-de1bba5ng-gis-toc3a0n-que1bb91c_-2.jpg"))
             .build()

         user!!.updateProfile(profileUpdates)
             .addOnCompleteListener { task ->
                 if (task.isSuccessful) {
                     Timber.i( "User profile updated.")
                 }
             }
 */


    }

    override fun onItemClick(data: ExchangeRateEntity?, position: Int, typeClick: Int) {

        val user = FirebaseAuth.getInstance().currentUser
        val exchange = String.format("%.4f", data?.rateUsd?.toDouble()).toDouble()

        MaterialDialog(requireContext()).show {

            if (data?.currencySymbol == "null") {
                title(text = " USD vs ${data.symbol.toUpperCase()}")
            } else {
                title(text = " USD vs ${data?.currencySymbol?.toUpperCase()}")
            }
            message(
                text = "${data?.symbol}  ${String.format(
                            "%.4f",
                            exchangeRateReverseCalculation(exchange)
                        )} = 1 USD"
            )
        }
    }

    fun filter(
        text: String,
        names: ArrayList<ExchangeRateEntity>,
        adapter: ExchangeAdapter
    ) {
        //new array list that will hold the filtered data
        val filteredList: ArrayList<ExchangeRateEntity> = ArrayList()

        //looping through existing elements
        for (s in names) {
            //if the existing elements contains the search input
            if (s.id.toLowerCase().contains(text.toLowerCase()) ||
                s.symbol.toLowerCase().contains(text.toLowerCase())
            ) {
                //adding the element to filtered list
                filteredList.add(s)
            }
        }

        //calling a method of the adapter class and passing the filtered list
        adapter.filterList(filteredList)


        /**
         *
         * TODO
         * https://proandroiddev.com/5-common-mistakes-when-using-architecture-components-403e9899f4cb
         * */
    }

    private fun setRecyclerViewScrollListener(
        activity: Activity,
        searchBox: EditText
    ): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                Extensions.hideKeyBoard(activity, searchBox)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
        exchange_list.adapter = adapter
    }

}