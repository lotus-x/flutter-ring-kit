package com.oryn.lotus.flutter_ring_kit

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.text.TextUtils
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.oryn.lotus.flutter_ring_kit.utils.Definitions
import io.flutter.plugin.common.EventChannel

class CallEventHandler(
    private val context: Context,
) : BroadcastReceiver(), EventChannel.StreamHandler {
    private var eventSink: EventChannel.EventSink? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        // check intent usable
        if (intent == null || context == null || TextUtils.isEmpty(intent.action)) return
        // check action
        if (intent.action == Definitions.ACTION_CALL_ACCEPT) {
            // get caller id
            val callerId = intent.getStringExtra(Definitions.EXTRA_CALLER_ID)
            // dispatch to stream
            eventSink?.success(mapOf(
                "type" to "call_answered",
                "callerId" to callerId
            ))
        }
    }

    override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
        // save sink
        eventSink = events
        // register broadcast listener
        registerBroadcastListener()
    }

    override fun onCancel(arguments: Any?) {
        eventSink = null
        // unregister broadcast receiver
        with(LocalBroadcastManager.getInstance(context)) {
            unregisterReceiver(this@CallEventHandler)
        }
    }

    private fun registerBroadcastListener() {
        // create intent filter
        val intentFilter = IntentFilter()
        intentFilter.addAction(Definitions.ACTION_CALL_ACCEPT)
        // register broadcast receiver
        with(LocalBroadcastManager.getInstance(context)) {
            registerReceiver(this@CallEventHandler, intentFilter)
        }
    }
}
