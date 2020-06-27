package com.fastcon.etherapp.data.remote.model

import com.google.gson.annotations.SerializedName



data class RateResponse(
    @SerializedName("data")
    val `data`: List<Data?>?,
    @SerializedName("timestamp")
    val timestamp: Long?
) {
    data class Data(
        @SerializedName("currencySymbol")
        val currencySymbol: Any?,
        @SerializedName("id")
        val id: String?,
        @SerializedName("rateUsd")
        val rateUsd: String?,
        @SerializedName("symbol")
        val symbol: String?,
        @SerializedName("type")
        val type: String?
    )
}