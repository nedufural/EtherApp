package com.fastcon.etherapp.ui.traded_volume

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fastcon.etherapp.data.DataManager
import com.fastcon.etherapp.data.remote.entity.TradedVolumeEntity
import com.fastcon.etherapp.data.remote.model.TradedVolumeResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TradedVolumeViewModel : ViewModel() {

    val tVolumeMutableLiveData: MutableLiveData<ArrayList<TradedVolumeEntity>> =
        MutableLiveData()
    val tVolumeErrorMsg: MutableLiveData<String> = MutableLiveData()
    var errorMsg: String = "error"
    var data = ArrayList<TradedVolumeEntity>()

    fun getTradedHistory() {

        val call: Call<TradedVolumeResponse> = DataManager.getApiService().getTradedHistory()
        call.enqueue(object : Callback<TradedVolumeResponse> {
            override fun onFailure(call: Call<TradedVolumeResponse>, t: Throwable) {
                tVolumeErrorMsg.value = errorMsg
            }

            override fun onResponse(
                call: Call<TradedVolumeResponse>,
                response: Response<TradedVolumeResponse>
            ) {
                var tChange24: String
                var tName: String
                var tRank: String
                var tVolume: String

                for (i in response.body()?.data!!) {
                    tChange24 = i?.changePercent24Hr.toString()
                    tName = i?.id.toString()
                    tRank = i?.rank.toString()
                    tVolume = i?.volumeUsd24Hr.toString()

                    data.add(TradedVolumeEntity(tChange24, tName, tRank, tVolume))
                }
                tVolumeMutableLiveData.value = data
            }
        })
    }

    fun getBitcoinTransaction(){

    }

}