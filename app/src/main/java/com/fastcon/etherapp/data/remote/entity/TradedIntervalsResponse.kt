package com.fastcon.etherapp.data.remote.entity
import com.google.gson.annotations.SerializedName


data class TradedIntervalsResponse(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("timestamp")
    val timestamp: Long
) {
    data class Data(
        @SerializedName("date")
        val date: String,
        @SerializedName("priceUsd")
        val priceUsd: String,
        @SerializedName("time")
        val time: Long
    )
}