package com.akshay.us

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.clevertap.android.sdk.*
import com.clevertap.android.sdk.displayunits.DisplayUnitListener
import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnit
import com.clevertap.android.sdk.pushnotification.CTPushNotificationListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import java.util.*


class MainActivity : AppCompatActivity() , CTInboxListener, DisplayUnitListener,
InAppNotificationButtonListener, CTPushNotificationListener {

    private lateinit var but: Button
    private lateinit var butSendPush: Button
    private lateinit var editTextTextPersonName: EditText
    private lateinit var editTextTextEmailAddress: EditText
    private lateinit var editTextPhone: EditText
    private lateinit var editTextDate: EditText
    private lateinit var rgroup: RadioGroup
    private lateinit var gender:String
    private var cleverTapDefaultInstance: CleverTapAPI? = null
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Check if activity is launched from notification click
        if (intent.extras != null && intent.extras!!.containsKey("appInbox")) {
            // Handle notification click
            Toast.makeText(this, "appInbox", Toast.LENGTH_SHORT).show()
        }

        rgroup= findViewById(R.id.radiogrp)

        editTextTextPersonName = findViewById(R.id.editTextTextPersonName)
        editTextTextEmailAddress = findViewById(R.id.editTextTextEmailAddress)
        editTextPhone = findViewById(R.id.editTextPhone)
        editTextDate = findViewById(R.id.editTextDate)
        // Sets up permissions request launcher.
        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {

            if (it) {
                showDummyNotification()
            } else {
                Snackbar.make(
                    findViewById<View>(android.R.id.content).rootView,
                    "Please grant Notification permission from App Settings",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
        createNotificationChannel()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            showDummyNotification()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
        initCleverTap()

        val profileUpdate = HashMap<String, Any>()
        profileUpdate["Key 2"] = "New Value"

        cleverTapDefaultInstance?.pushProfile(profileUpdate)

//        cleverTapDefaultInstance?.addMultiValueForKey("Key 2","Key 2")

        gender=""
        but=findViewById(R.id.button)
        butSendPush=findViewById(R.id.button2)

        if (rgroup != null) {
            rgroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
                gender += if (R.id.radiomale === checkedId) "M" else "F"
                Toast.makeText(applicationContext, gender, Toast.LENGTH_SHORT).show()
            })
        }

        but.setOnClickListener {
            mUserLogin()
        }
        butSendPush.setOnClickListener {
            cleverTapDefaultInstance?.getProperty("MSG-push")
            cleverTapDefaultInstance?.pushEvent("Check CTid")
        }

    }

//552ca77507bb4c468ab1fbac6985e20d

    private fun initCleverTap() {

        //Set Debug level for CleverTap
        CleverTapAPI.setDebugLevel(CleverTapAPI.LogLevel.DEBUG)
        //Create CleverTap's default instance
        cleverTapDefaultInstance = CleverTapAPI.getDefaultInstance(applicationContext)
        cleverTapDefaultInstance?.setInAppNotificationButtonListener(this@MainActivity)
        cleverTapDefaultInstance?.apply {
            enableDeviceNetworkInfoReporting(true)
            //Set the Notification Inbox Listener
            ctNotificationInboxListener = this@MainActivity
            //Set the Display Unit Listener
            setDisplayUnitListener(this@MainActivity)
            //Initialize the inbox and wait for callbacks on overridden methods
            initializeInbox()
        }
        cleverTapDefaultInstance?.recordScreen("recordScreen")
        cleverTapDefaultInstance?.showAppInbox()
        cleverTapDefaultInstance?.getCleverTapID {
                Log.i("TAG", "setting object id to firebase : ${it}")
                FirebaseAnalytics.getInstance(this).setUserProperty("ct_objectId", it)
        }


    }
///Users/akshaya.jain/AndroidStudioProjects/MyApplication2/app
    override fun inboxDidInitialize() {
        
    }

    override fun inboxMessagesDidUpdate() {
        
    }

    override fun onDisplayUnitsLoaded(units: ArrayList<CleverTapDisplayUnit>?) {
        
    }

    override fun onInAppButtonClick(payload: HashMap<String, String>?) {
        Toast.makeText(this, "Button Clicked", Toast.LENGTH_SHORT).show()
    }

    private fun createNotificationChannel() {
        Toast.makeText(this, "Permission granted!", Toast.LENGTH_SHORT).show()

        //For Android 13+ we need to create notification channel after notification permission is accepted
        CleverTapAPI.createNotificationChannel(
            this, "AKSHAY", "Core",
            "Core notifications", NotificationManager.IMPORTANCE_MAX, true
        )

        CleverTapAPI.createNotificationChannel(
            this, "AKSHAY_PT", "Push templates",
            "All push templates", NotificationManager.IMPORTANCE_MAX, true
        )
    }

    private fun showDummyNotification() {
        val builder = NotificationCompat.Builder(this, "AKSHAY")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Congratulations! 🎉🎉🎉")
            .setContentText("You have post a notification to Android 13!!!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(this@MainActivity, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                return
            }
//            notify(1, builder.build())
        }
    }

    private fun mUserLogin(){
        val profileUpdate = HashMap<String, Any>()
        profileUpdate["Name"] = "Akshay"//editTextTextPersonName.text.toString() // String
        profileUpdate["Identity"] = "9591890116"//editTextPhone.text.toString() // String or number
        profileUpdate["Email"] = "akshaya.jain@clevertap.com"//editTextTextEmailAddress.text.toString() // Email address of the user
        profileUpdate["Phone"] = "+919591890116"//editTextPhone.text.toString() // Phone (with the country code, starting with +)
        profileUpdate["Gender"] = "M" //gender // Can be either M or F
        profileUpdate["DOB"] = Date() // Date of Birth. Set the Date object to the appropriate value first
        profileUpdate["Photo"] = "www.foobar.com/image.jpeg" // URL to the Image

// optional fields. controls whether the user will be sent email, push etc.
        profileUpdate["MSG-email"] = false // Disable email notifications
        profileUpdate["MSG-push"] = true // Enable push notifications
        profileUpdate["MSG-sms"] = false // Disable SMS notifications
        profileUpdate["MSG-dndPhone"] = true // Opt out phone
        profileUpdate["MSG-dndEmail"] = true // Opt out email
        profileUpdate["MyStuff"] = arrayListOf("bag", "shoes") //ArrayList of Strings
        profileUpdate["MyStuff"] = arrayOf("Jeans", "Perfume") //String Array
        cleverTapDefaultInstance?.onUserLogin(profileUpdate)
//        cleverTapDefaultInstance?.pushProfile(profileUpdate)

    }

    override fun onNotificationClickedPayloadReceived(payload: java.util.HashMap<String, Any>?) {
        Log.d("payload",payload.toString())
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.d("onNewIntent","onNewIntent")

        // start your activity by passing the intent
        startActivity(Intent(this, AppInBoxActivity::class.java).apply {})

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            cleverTapDefaultInstance?.pushNotificationClickedEvent(intent!!.extras)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            NotificationUtils.dismissNotification(intent, applicationContext)
        }

    }
}