package com.akshay.us

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import android.util.Log
import com.clevertap.android.pushtemplates.PushTemplateNotificationHandler
import com.clevertap.android.sdk.CleverTapAPI
import com.clevertap.android.sdk.pushnotification.CTPushNotificationListener
import com.clevertap.android.sdk.ActivityLifecycleCallback
import com.clevertap.android.sdk.InAppNotificationButtonListener
import com.clevertap.android.sdk.interfaces.NotificationHandler
import java.util.HashMap

class App: Application(), CTPushNotificationListener,ActivityLifecycleCallbacks{

    override fun onCreate() {
        ActivityLifecycleCallback.register(this)
        CleverTapAPI.setDebugLevel(CleverTapAPI.LogLevel.DEBUG)
        CleverTapAPI.setNotificationHandler(PushTemplateNotificationHandler() as NotificationHandler)
        registerActivityLifecycleCallbacks(this)
        super.onCreate()
    }

    override fun onNotificationClickedPayloadReceived(payload: HashMap<String, Any>?) {
        Log.i("Clevertap", "onNotificationClickedPayloadReceived = $payload")
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        Log.i("Clevertap", "onActivityCreated")
    }

    override fun onActivityStarted(activity: Activity) {
        Log.i("Clevertap", "onActivityStarted")
    }

    override fun onActivityResumed(activity: Activity) {
        Log.i("Clevertap", "onActivityResumed")
    }

    override fun onActivityPaused(activity: Activity) {
        Log.i("Clevertap", "onActivityPaused")
    }

    override fun onActivityStopped(activity: Activity) {
        Log.i("Clevertap", "onActivityStopped")
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        Log.i("Clevertap", "onActivitySaveInstanceState")
    }

    override fun onActivityDestroyed(activity: Activity) {
        Log.i("Clevertap", "onActivityDestroyed")
    }

//    override fun onInAppButtonClick(p0: HashMap<String, String>?) {
//        Log.i("Clevertap", "onInAppButtonClick")
//    }
}