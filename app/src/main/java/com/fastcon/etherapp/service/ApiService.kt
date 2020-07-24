package com.fastcon.etherapp.service

import com.fastcon.etherapp.notification.PushNotification
import com.fastcon.etherapp.data.remote.entity.TradedIntervalsResponse
import com.fastcon.etherapp.data.remote.entity.BitcoinBalanceResponse
import com.fastcon.etherapp.data.remote.model.*
import com.fastcon.etherapp.util.common.Commons.CONTENT_TYPE
import com.fastcon.etherapp.util.common.Commons.SERVER_KEY
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


@JvmSuppressWildcards
interface ApiService {
    @FormUrlEncoded
    @POST("api/login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>


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

    /**
     * implemented push notification with kotlin coroutines
     * */
    @Headers("Authorization: key=$SERVER_KEY", "Content-Type:$CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(
        @Body notification: PushNotification
    ): Response<ResponseBody>


}
