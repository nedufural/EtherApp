package com.fastcon.etherapp.data

import com.facebook.FacebookSdk
import com.fastcon.etherapp.service.ApiClient
import com.fastcon.etherapp.service.ApiService

open class DataManager {


    companion object {

        fun getApiService(): ApiService = ApiClient.getClient()
            .create(ApiService::class.java)


        fun getNotificationApiService(): ApiService = ApiClient.getNotificationClient()
            .create(ApiService::class.java)



    }
}