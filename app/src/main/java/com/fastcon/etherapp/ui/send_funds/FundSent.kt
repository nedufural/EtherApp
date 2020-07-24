package com.fastcon.etherapp.ui.send_funds

import com.fastcon.etherapp.notification.PushNotification
import com.fastcon.etherapp.data.DataManager
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class FundSent{
    companion object {
        fun sendNotification(notification: PushNotification) =
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = DataManager.getNotificationApiService().postNotification(notification)
                        Timber.d("response: $response")
                } catch (e: Exception) {
                    Timber.e(e.toString())
                }
            }
    }

}