package com.fastcon.etherapp.ui.exchange


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fastcon.etherapp.data.DataManager.Companion.getApiService
import com.fastcon.etherapp.data.model.entity.ExchangeRateEntity
import com.fastcon.etherapp.data.remote.model.RateResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber


class ExchangeViewModel : ViewModel() {
    private val _exchangeRateMutableLiveData: MutableLiveData<ArrayList<ExchangeRateEntity>> =
        MutableLiveData()
    val exchangeRateMutableLiveData: LiveData<ArrayList<ExchangeRateEntity>> =
        _exchangeRateMutableLiveData
    val exchangeErrorMsg: MutableLiveData<String> = MutableLiveData()
    var errorMsg: String = "error"
    var data = ArrayList<ExchangeRateEntity>()

    init {
        Timber.i("VieModel created!")
    }

    override fun onCleared() {
        super.onCleared()
        Timber.i("VieModel deleted!")
    }

    fun getExchangeRates() {

        val call: Call<RateResponse> = getApiService().getExchangeRates()
        call.enqueue(object : Callback<RateResponse> {
            override fun onFailure(call: Call<RateResponse>, t: Throwable) {
                exchangeErrorMsg.postValue(errorMsg)
            }

            override fun onResponse(call: Call<RateResponse>, response: Response<RateResponse>) {
                data.clear()
                var currencySymbol: String
                var currencyId: String
                var symbol: String
                var type: String
                var rateUsd: String

                for (i in response.body()?.data!!) {
                    currencySymbol = i?.currencySymbol.toString()
                    currencyId = i?.id.toString()
                    symbol = i?.symbol.toString()
                    rateUsd = i?.rateUsd.toString()
                    type = i?.type.toString()

                    data.add(ExchangeRateEntity(currencySymbol, currencyId, rateUsd, symbol, type))
                }
                _exchangeRateMutableLiveData.value = data
            }
        })
    }

}
