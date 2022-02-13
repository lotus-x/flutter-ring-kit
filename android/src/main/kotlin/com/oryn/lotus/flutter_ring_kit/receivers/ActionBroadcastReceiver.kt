package com.oryn.lotus.flutter_ring_kit.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.core.app.NotificationManagerCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.oryn.lotus.flutter_ring_kit.helpers.IntentHelper
import com.oryn.lotus.flutter_ring_kit.utils.Definitions

class ActionBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        // check intent usable
        if (intent == null || context == null || TextUtils.isEmpty(intent.action)) return
        // check action
        when (intent.action) {
            Definitions.ACTION_CALL_ACCEPT -> {
                // get extras
                val extras = intent.extras ?: return
                // check extras exist
                val callerId = extras.getString(Definitions.EXTRA_CALLER_ID) ?: return
                // send local broadcast intent
                val localBundle = Bundle()
                localBundle.putString(Definitions.EXTRA_CALLER_ID, callerId)
                val localIntent = Intent(Definitions.ACTION_CALL_ACCEPT).apply {
                    putExtras(localBundle)
                }
                with(LocalBroadcastManager.getInstance(context.applicationContext)) {
                    sendBroadcast(localIntent)
                }
                // close notification
                with(NotificationManagerCompat.from(context)) {
                    cancel(callerId.hashCode())
                }
                // get launch intent
                val launchIntent = IntentHelper.getLaunchIntent(context) ?: return
                // add extras
                launchIntent.putExtra(
                    Definitions.EXTRA_LAUNCHED_ACTION,
                    Definitions.EXTRA_ACTION_CALL_ACCEPT
                )
                context.startActivity(launchIntent)
            }
            Definitions.ACTION_CALL_REJECT -> {
                // get extras
                val extras = intent.extras ?: return
                // check extras exist
                val callerId = extras.getString(Definitions.EXTRA_CALLER_ID) ?: return
                // send local broadcast intent
                val localBundle = Bundle()
                localBundle.putString(Definitions.EXTRA_CALLER_ID, callerId)
                val localIntent = Intent(Definitions.ACTION_CALL_REJECT).apply {
                    putExtras(localBundle)
                }
                with(LocalBroadcastManager.getInstance(context.applicationContext)) {
                    sendBroadcast(localIntent)
                }
                // close notification
                with(NotificationManagerCompat.from(context)) {
                    cancel(callerId.hashCode())
                }
            }
        }
    }
}
