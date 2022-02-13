package com.oryn.lotus.flutter_ring_kit

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.oryn.lotus.flutter_ring_kit.receivers.ActionBroadcastReceiver
import com.oryn.lotus.flutter_ring_kit.utils.Definitions

class CallerActivity : Activity() {
    private lateinit var broadcastReceiver: BroadcastReceiver

    private var callerId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            resources.getIdentifier(
                "caller_screen",
                "layout",
                packageName
            )
        )
        storeIntentData(intent)
        registerBroadcastListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        // unregister broadcast receiver
        with(LocalBroadcastManager.getInstance(this)) {
            unregisterReceiver(broadcastReceiver)
        }
    }

    private fun storeIntentData(intent: Intent) {
        callerId = intent.getStringExtra(Definitions.EXTRA_CALLER_ID)
    }

    private fun registerBroadcastListener() {
        // create broadcast receiver
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                // check intent usable
                if (intent == null || context == null || TextUtils.isEmpty(intent.action)) return
                // close activity upon any action
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAndRemoveTask()
                } else {
                    finish()
                }
            }
        }
        // create intent filter
        val intentFilter = IntentFilter()
        intentFilter.addAction(Definitions.ACTION_CALL_ACCEPT)
        intentFilter.addAction(Definitions.ACTION_CALL_REJECT)
        // register broadcast receiver
        with(LocalBroadcastManager.getInstance(this)) {
            registerReceiver(broadcastReceiver, intentFilter)
        }
    }

    fun onAcceptCall(view: View?) {
        val bundle = Bundle()
        bundle.putString(Definitions.EXTRA_CALLER_ID, callerId)
        val intent = Intent(this, ActionBroadcastReceiver::class.java).apply {
            action = Definitions.ACTION_CALL_ACCEPT
            putExtras(bundle)
        }
        applicationContext.sendBroadcast(intent)
    }

    fun onRejectCall(view: View?) {
        val bundle = Bundle()
        bundle.putString(Definitions.EXTRA_CALLER_ID, callerId)
        val intent = Intent(this, ActionBroadcastReceiver::class.java).apply {
            action = Definitions.ACTION_CALL_REJECT
            putExtras(bundle)
        }
        applicationContext.sendBroadcast(intent)
    }
}
