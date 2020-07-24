package com.fastcon.etherapp.service


import com.fastcon.etherapp.util.common.Commons
import com.fastcon.etherapp.util.common.Commons.Notification_Base_Url
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.util.Collections.singletonList
import java.util.concurrent.TimeUnit


internal class RSDeserializer : JsonDeserializer<List<String>> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): List<String> {

        return if (json is JsonArray) {
            var arrayList = arrayListOf<String>()
            for (x in 0 until json.size()) {
                arrayList.add(context.deserialize(json.elementAt(x), String::class.java))
            }
            return arrayList
        } else {
            val boolean1 = context.deserialize<String>(json, String::class.java)
            singletonList(boolean1.toString())
        }
    }
}

object ApiClient {
    private const val REQUEST_TIMEOUT = 60

    fun getClient(): Retrofit {
        val t = object : TypeToken<List<String>>() {}.type

        val gSon = GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .registerTypeAdapter(t, RSDeserializer())
            .create()

        return Retrofit.Builder()
            .baseUrl(Commons.Base_Url)
            .client(initOkHttp())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gSon))
            .build()
    }


    fun getNotificationClient(): Retrofit {

        val gSon = GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
            .create()

        return Retrofit.Builder()
            .baseUrl(Notification_Base_Url)
            .client(initOkHttp())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gSon))
            .build()
    }

    private fun initOkHttp(): OkHttpClient {
        val httpClient = OkHttpClient().newBuilder()
            .connectTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)

        val dispatcher = Dispatcher()
        dispatcher.maxRequests = 1
        httpClient.dispatcher(dispatcher)


        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        httpClient.addInterceptor(interceptor)

        return httpClient.build()
    }
}

