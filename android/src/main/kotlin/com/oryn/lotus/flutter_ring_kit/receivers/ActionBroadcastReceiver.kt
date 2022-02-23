package com.oryn.lotus.flutter_ring_kit.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import androidx.core.app.NotificationManagerCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.oryn.lotus.flutter_ring_kit.models.RingerData
import com.oryn.lotus.flutter_ring_kit.notifications.NotificationBuilder
import com.oryn.lotus.flutter_ring_kit.utils.Definitions

class ActionBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        // check intent usable
        if (intent == null || context == null || TextUtils.isEmpty(intent.action)) return
        // check action
        when (intent.action) {
            Definitions.ACTION_CALL_REJECT -> {
                // get extras
                val extras = intent.extras ?: return
                // create data from extra
                val ringerData = RingerData.fromBundle(extras)
                // send local broadcast intent
                val localIntent = Intent(Definitions.ACTION_CALL_REJECT).apply {
                    putExtras(ringerData.toBundle())
                }
                with(LocalBroadcastManager.getInstance(context.applicationContext)) {
                    sendBroadcast(localIntent)
                }
                // close notification
                with(NotificationManagerCompat.from(context)) {
                    cancel(ringerData.callerId.hashCode())
                }
            }
            Definitions.ACTION_CALL_TIMED_OUT -> {
                // get extras
                val extras = intent.extras ?: return
                // create data from extra
                val ringerData = RingerData.fromBundle(extras)
                // send local broadcast intent
                val localIntent = Intent(Definitions.ACTION_CALL_TIMED_OUT).apply {
                    putExtras(ringerData.toBundle())
                }
                with(LocalBroadcastManager.getInstance(context.applicationContext)) {
                    sendBroadcast(localIntent)
                }
                // create notification builder
                val notificationBuilder = NotificationBuilder(context)
                // show missed call notification
                notificationBuilder.showMissedCallNotification(ringerData)
            }
        }
    }
}
