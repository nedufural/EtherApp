package com.fastcon.etherapp.data.remote.model
import com.google.gson.annotations.SerializedName


data class TradedVolumeResponse(
    @SerializedName("data")
    val `data`: List<Data?>?,
    @SerializedName("timestamp")
    val timestamp: Long?
) {
    data class Data(
        @SerializedName("changePercent24Hr")
        val changePercent24Hr: String?,
        @SerializedName("id")
        val id: String?,
        @SerializedName("marketCapUsd")
        val marketCapUsd: String?,
        @SerializedName("maxSupply")
        val maxSupply: String?,
        @SerializedName("name")
        val name: String?,
        @SerializedName("priceUsd")
        val priceUsd: String?,
        @SerializedName("rank")
        val rank: String?,
        @SerializedName("supply")
        val supply: String?,
        @SerializedName("symbol")
        val symbol: String?,
        @SerializedName("volumeUsd24Hr")
        val volumeUsd24Hr: String?,
        @SerializedName("vwap24Hr")
        val vWap24Hr: String?
    )
}