package com.fastcon.etherapp.ui.exchange


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.facebook.FacebookSdk.getApplicationContext
import com.fastcon.etherapp.R

import com.fastcon.etherapp.data.DataManager.Companion.getApiService
import com.fastcon.etherapp.data.model.entity.ExchangeRateEntity
import com.fastcon.etherapp.data.remote.entity.User
import com.fastcon.etherapp.data.remote.model.RateResponse
import com.fastcon.etherapp.exception.NetworkExceptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber


class ExchangeViewModel : ViewModel() {

    private var exchangeRateMutableLiveData: MutableLiveData<ArrayList<ExchangeRateEntity>>? = null
    fun getExchangeRateMutableLiveData(): LiveData<ArrayList<ExchangeRateEntity>>? {
        if (exchangeRateMutableLiveData == null) {
            exchangeRateMutableLiveData = MutableLiveData()
        }
        return exchangeRateMutableLiveData
    }

    private var exchangeErrorMsg: MutableLiveData<String>? = null
    fun getExchangeErrorMsg(): LiveData<String>? {
        if (exchangeErrorMsg == null) {
            exchangeErrorMsg = MutableLiveData()
        }
        return exchangeErrorMsg
    }
    var errorMsg: String = getApplicationContext().getString(R.string.error)
    var data = ArrayList<ExchangeRateEntity>()


    fun getExchangeRates() {
        val call: Call<RateResponse> = getApiService().getExchangeRates()
        call.enqueue(object : Callback<RateResponse> {
            override fun onFailure(call: Call<RateResponse>, t: Throwable) {
                exchangeErrorMsg?.postValue(errorMsg)
            }

            override fun onResponse(call: Call<RateResponse>, response: Response<RateResponse>) {
                data.clear()
                var currencySymbol: String
                var currencyId: String
                var symbol: String
                var type: String
                var rateUsd: String

                val responseCode = NetworkExceptions.getException(response.code())
                if (response.code() == 200) {
                    if (response.body() != null) {
                        for (i in response.body()?.data!!) {
                            currencySymbol = i?.currencySymbol.toString()
                            currencyId = i?.id.toString()
                            symbol = i?.symbol.toString()
                            rateUsd = i?.rateUsd.toString()
                            type = i?.type.toString()
                            data.add(ExchangeRateEntity(currencySymbol, currencyId, rateUsd, symbol, type))
                        }
                        exchangeRateMutableLiveData?.value = data
                    }
                    else{
                        exchangeErrorMsg?.value = getApplicationContext().getString(R.string.response_empty)
                    }
                }
                else{
                    exchangeErrorMsg?.value = responseCode
                }
            }
        })
    }

}
