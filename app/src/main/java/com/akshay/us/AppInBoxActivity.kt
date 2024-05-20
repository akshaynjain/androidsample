package com.akshay.us

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.clevertap.android.sdk.CTInboxListener
import com.clevertap.android.sdk.CleverTapAPI
import com.clevertap.android.sdk.InboxMessageButtonListener
import java.util.HashMap

class AppInBoxActivity : AppCompatActivity() , CTInboxListener, InboxMessageButtonListener {

    var cleverTapDefaultInstance: CleverTapAPI? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_in_box)

        CleverTapAPI.setDebugLevel(CleverTapAPI.LogLevel.DEBUG)
        cleverTapDefaultInstance = CleverTapAPI.getDefaultInstance(applicationContext)

        cleverTapDefaultInstance?.apply {
            ctNotificationInboxListener = this@AppInBoxActivity
            initializeInbox()
        }
    }

    override fun inboxDidInitialize() {

    }

    override fun inboxMessagesDidUpdate() {

    }

    override fun onInboxButtonClick(payload: HashMap<String, String>?) {

    }
}