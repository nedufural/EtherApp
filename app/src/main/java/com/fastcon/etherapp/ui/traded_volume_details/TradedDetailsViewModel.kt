package com.fastcon.etherapp.ui.traded_volume_details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fastcon.etherapp.data.DataManager
import com.fastcon.etherapp.data.local.PrefUtils
import com.fastcon.etherapp.data.remote.model.TradedDetailsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TradedDetailsViewModel : ViewModel() {
    val tVolumeDetailMutableLiveData: MutableLiveData<TradedDetailsResponse.Data> =
        MutableLiveData()
    val tVolumeDetailsErrorMsg: MutableLiveData<String> = MutableLiveData()
    lateinit var errorMsg: String

    fun getTradedDetails(id: String) {

        val call: Call<TradedDetailsResponse> = DataManager.getApiService().getTradedDetails(id)
        call.enqueue(object : Callback<TradedDetailsResponse> {
            override fun onFailure(call: Call<TradedDetailsResponse>, t: Throwable) {
                tVolumeDetailsErrorMsg.value = errorMsg
            }

            override fun onResponse(
                call: Call<TradedDetailsResponse>,
                response: Response<TradedDetailsResponse>
            ) {
                PrefUtils.saveTradedHistoryDetailTime(response.body()?.timestamp)
                tVolumeDetailMutableLiveData.value = response.body()?.data
            }
        })

    }
}