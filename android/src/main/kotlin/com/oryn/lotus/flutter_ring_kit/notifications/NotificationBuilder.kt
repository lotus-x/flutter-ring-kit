package com.oryn.lotus.flutter_ring_kit.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.oryn.lotus.flutter_ring_kit.CallerActivity
import com.oryn.lotus.flutter_ring_kit.helpers.StringHelper
import com.oryn.lotus.flutter_ring_kit.receivers.ActionBroadcastReceiver
import com.oryn.lotus.flutter_ring_kit.utils.Definitions

class NotificationBuilder(private val context: Context) {

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        // init channel
        val channel = NotificationChannel(
            "test",
            "Test",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            // set description
            description = "Testing notifications"

            // get ringtone sound
            val ringtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
            // create attributes
            val audioAttributes = AudioAttributes.Builder().apply {
                setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
            }.build()
            setSound(ringtone, audioAttributes)
        }
        // create channel
        with(NotificationManagerCompat.from(context)) {
            createNotificationChannel(channel)
        }
    }

    fun showRingNotification(callerId: String) {
        // create builder
        val builder = NotificationCompat.Builder(context, "test")
            .setContentTitle("Incoming Video Consultation")
            .setContentText("Dr. Megahead Pillow, Appointment Number #3")
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setSmallIcon(context.applicationInfo.icon)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOngoing(true)
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .setTimeoutAfter(60000)
        // add actions
        addCallAcceptAction(callerId, builder)
        addCallRejectAction(callerId, builder)
        // set additional details
        setSound(builder)
        setFullScreenIntent(callerId, builder)
        // show notification
        with(NotificationManagerCompat.from(context)) {
            notify(callerId.hashCode(), builder.build())
        }
    }

    private fun setSound(builder: NotificationCompat.Builder) {
        // get ringtone
        val ringtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        // set builder sound
        builder.setSound(ringtone)
    }

    private fun setFullScreenIntent(callerId: String, builder: NotificationCompat.Builder) {
        val bundle = Bundle()
        bundle.putString(Definitions.EXTRA_CALLER_ID, callerId)
        val intent = Intent(context, CallerActivity::class.java).apply {
            putExtras(bundle)
        }
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        } else {
            PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        builder.setFullScreenIntent(pendingIntent, true)
    }

    private fun addCallAcceptAction(callerId: String, builder: NotificationCompat.Builder) {
        val bundle = Bundle()
        bundle.putString(Definitions.EXTRA_CALLER_ID, callerId)
        val intent = Intent(context, ActionBroadcastReceiver::class.java).apply {
            action = Definitions.ACTION_CALL_ACCEPT
            putExtras(bundle)
        }
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        } else {
            PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        val action = NotificationCompat.Action.Builder(
            android.R.drawable.ic_menu_call,
            StringHelper.getColorizedText("Join", "#4CB050"),
            pendingIntent
        ).build()
        // add action to the notification builder
        builder.addAction(action)
    }

    private fun addCallRejectAction(callerId: String, builder: NotificationCompat.Builder) {
        val bundle = Bundle()
        bundle.putString(Definitions.EXTRA_CALLER_ID, callerId)
        val intent = Intent(context, ActionBroadcastReceiver::class.java).apply {
            action = Definitions.ACTION_CALL_REJECT
            putExtras(bundle)
        }
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        } else {
            PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        val action = NotificationCompat.Action.Builder(
            android.R.drawable.ic_menu_close_clear_cancel,
            StringHelper.getColorizedText("Ignore", "#E02B00"),
            pendingIntent
        ).build()
        // add action to the notification builder
        builder.addAction(action)
    }
}
