package com.fastcon.etherapp.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fastcon.etherapp.data.DataManager
import com.fastcon.etherapp.data.remote.model.ConversionResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UsdConversionViewModel : ViewModel() {
    var errorMsg: String = "error"

    var conversionResult: MutableLiveData<ConversionResponse>? = MutableLiveData()
    var conversionError: MutableLiveData<String?>? = MutableLiveData()
    fun getConversionRates(url: String) {
        val call: Call<ConversionResponse> = DataManager.getApiService().getConversionRates(url)
        call.enqueue(object : Callback<ConversionResponse> {
            override fun onFailure(call: Call<ConversionResponse>, t: Throwable) {
                conversionError?.value = errorMsg
                println("failed")
            }

            override fun onResponse(call: Call<ConversionResponse>, response: Response<ConversionResponse>) {
                val result = response.body()
                println(result?.data?.rateUsd)
                conversionResult?.value = result

            }
        })
    }
}