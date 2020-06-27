package com.fastcon.etherapp.service

import com.fastcon.etherapp.data.remote.entity.TradedIntervalsResponse
import com.fastcon.etherapp.data.remote.entity.BitcoinBalanceResponse
import com.fastcon.etherapp.data.remote.NoticeResponse
import com.fastcon.etherapp.data.remote.UploadDeviceToken
import com.fastcon.etherapp.data.remote.model.*
import retrofit2.Call
import retrofit2.http.*


@JvmSuppressWildcards
interface ApiService {
    @FormUrlEncoded
    @POST("api/login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @PATCH("device_relations")
    fun updateDeviceToken(@Body request: UploadDeviceToken): Call<NoticeResponse>


    @GET("v2/rates")
    fun getExchangeRates(): Call<RateResponse>

    @GET("v2/assets")
    fun getTradedHistory(): Call<TradedVolumeResponse>

    @GET("v2/assets/{id}")
    fun getTradedDetails(@Path("id") id: String): Call<TradedDetailsResponse>
    @GET("v2/assets/{id}/history?interval=d1")
    fun getTradedHistoryIntervals(@Path("id") id: String): Call<TradedIntervalsResponse>

    @Headers("x-api-key: ed3a6937cc67a800c3088ced93fc7be1")
    @GET
    fun getNews(@Url url: String): Call<NewsResponse>


    @GET
    fun getTransferHistory(@Url url: String): Call<TransferResponse>

    @GET
    fun getBitcoinTxHistory(@Url url: String): Call<BitcoinResponse>
    //get bitcoin balance
    /**
     * https://sochain.com/api/v2/get_address_balance/BITCOIN/DFundmtrigzA6E25Swr2pRe4Eb79bGP8G1
     * */
    @GET
    fun getBitcoinBalance(@Url url: String): Call<BitcoinBalanceResponse>
}
