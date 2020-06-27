package com.fastcon.etherapp.data.remote.entity
import com.google.gson.annotations.SerializedName


data class BitcoinBalanceResponse(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("status")
    val status: String) {
    data class Data(
        @SerializedName("address")
        val address: String,
        @SerializedName("confirmed_balance")
        val confirmedBalance: String,
        @SerializedName("network")
        val network: String,
        @SerializedName("unconfirmed_balance")
        val unconfirmedBalance: String
    )
}