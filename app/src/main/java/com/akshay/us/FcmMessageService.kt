package com.akshay.us

import com.clevertap.android.sdk.CleverTapAPI
import com.clevertap.android.sdk.pushnotification.fcm.CTFcmMessageHandler
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FcmMessageService:FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        CTFcmMessageHandler()
            .createNotification(applicationContext, message)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        CleverTapAPI.getDefaultInstance(this)?.apply {
            pushFcmRegistrationId(token, true)
        }
    }
}