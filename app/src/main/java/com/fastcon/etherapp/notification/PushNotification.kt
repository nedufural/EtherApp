package com.fastcon.etherapp.notification

import com.fastcon.etherapp.notification.NotificationData

data class PushNotification (
    val data: NotificationData,
    val to:String
)