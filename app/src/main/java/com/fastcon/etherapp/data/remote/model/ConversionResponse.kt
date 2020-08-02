package com.fastcon.etherapp.data.remote.model;

import com.google.gson.annotations.SerializedName

data class ConversionResponse
    (
    @SerializedName("data") val data: Data,
    @SerializedName("timestamp") val timestamp: Int
)

data class Data(
    @SerializedName("id") val id: String,
    @SerializedName("symbol") val symbol: String,
    @SerializedName("currencySymbol") val currencySymbol: String,
    @SerializedName("type") val type: String,
    @SerializedName("rateUsd") val rateUsd: Double


)

