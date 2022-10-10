package com.oryn.lotus.flutter_ring_kit.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.oryn.lotus.flutter_ring_kit.CallerActivity
import com.oryn.lotus.flutter_ring_kit.ReminderActivity
import com.oryn.lotus.flutter_ring_kit.helpers.IntentHelper
import com.oryn.lotus.flutter_ring_kit.helpers.StringHelper
import com.oryn.lotus.flutter_ring_kit.models.NotificationChannelData
import com.oryn.lotus.flutter_ring_kit.models.ReminderData
import com.oryn.lotus.flutter_ring_kit.models.RingerData
import com.oryn.lotus.flutter_ring_kit.receivers.ActionBroadcastReceiver
import com.oryn.lotus.flutter_ring_kit.utils.Definitions

class NotificationBuilder(private val context: Context) {
    fun createNotificationChannels(channelData: NotificationChannelData) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        // init channels
        val ringingChannel = NotificationChannel(
            channelData.ringerChannelId,
            channelData.ringerChannelName,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            // set description
            description = channelData.ringerChannelDescription

            // get ringtone sound
            val ringtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
            // create attributes
            val audioAttributes = AudioAttributes.Builder().apply {
                setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
            }.build()
            setSound(ringtone, audioAttributes)
        }
        val remindingChannel = NotificationChannel(
            channelData.reminderChannelId,
            channelData.reminderChannelName,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            // set description
            description = channelData.reminderChannelDescription

            // get ringtone sound
            val ringtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
            // create attributes
            val audioAttributes = AudioAttributes.Builder().apply {
                setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
            }.build()
            setSound(ringtone, audioAttributes)
        }
        val missedCallChannel = NotificationChannel(
            channelData.missedCallChannelId,
            channelData.missedCallChannelName,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            // set description
            description = channelData.missedCallChannelDescription
        }
        val missedReminderChannel = NotificationChannel(
            channelData.missedReminderChannelId,
            channelData.missedReminderChannelName,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            // set description
            description = channelData.missedReminderChannelDescription
        }
        // create channels
        with(NotificationManagerCompat.from(context)) {
            createNotificationChannels(
                listOf(
                    ringingChannel, remindingChannel, missedCallChannel, missedReminderChannel
                )
            )
        }
    }

    fun showRingNotification(ringerData: RingerData) {
        // create builder
        val builder = NotificationCompat.Builder(context, ringerData.notificationChannelId)
            .setContentTitle(ringerData.notificationTitle)
            .setContentText(ringerData.notificationDescription)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC).setOngoing(true)
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .setTimeoutAfter(ringerData.notificationTimeout.toLong())
            .setStyle(NotificationCompat.BigTextStyle().bigText(ringerData.notificationDescription))
        // add actions
        addCallRejectAction(ringerData, builder)
        addCallAcceptAction(ringerData, builder)
        // set additional details
        setCallerIcon(ringerData, builder)
        setCallerColor(ringerData, builder)
        setSound(builder)
        setCallerFullScreenIntent(ringerData, builder)
        setCallerDeleteIntent(ringerData, builder)
        // show notification
        with(NotificationManagerCompat.from(context)) {
            notify(ringerData.callerId.hashCode(), builder.build())
        }
    }

    fun showRemindNotification(reminderData: ReminderData) {
        // create builder
        val builder = NotificationCompat.Builder(context, reminderData.notificationChannelId)
            .setContentTitle(reminderData.notificationTitle)
            .setContentText(reminderData.notificationDescription)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC).setOngoing(true)
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .setTimeoutAfter(reminderData.notificationTimeout.toLong()).setStyle(
                NotificationCompat.BigTextStyle().bigText(reminderData.notificationDescription)
            )
        // add actions
        addReminderRejectAction(reminderData, builder)
        addReminderAcceptAction(reminderData, builder)
        // set additional details
        setReminderIcon(reminderData, builder)
        setReminderColor(reminderData, builder)
        setSound(builder)
        setReminderFullScreenIntent(reminderData, builder)
        setReminderDeleteIntent(reminderData, builder)
        // show notification
        with(NotificationManagerCompat.from(context)) {
            notify(reminderData.reminderId.hashCode(), builder.build())
        }
    }

    private fun setCallerIcon(ringerData: RingerData, builder: NotificationCompat.Builder) {
        if (ringerData.notificationIcon == null) {
            builder.setSmallIcon(context.applicationInfo.icon)
        } else {
            // get icon
            val iconId = context.resources.getIdentifier(
                ringerData.notificationIcon, "drawable", context.packageName
            )
            // check exist
            if (iconId != 0) builder.setSmallIcon(iconId) else builder.setSmallIcon(context.applicationInfo.icon)
        }
    }

    private fun setReminderIcon(reminderData: ReminderData, builder: NotificationCompat.Builder) {
        if (reminderData.notificationIcon == null) {
            builder.setSmallIcon(context.applicationInfo.icon)
        } else {
            // get icon
            val iconId = context.resources.getIdentifier(
                reminderData.notificationIcon, "drawable", context.packageName
            )
            // check exist
            if (iconId != 0) builder.setSmallIcon(iconId) else builder.setSmallIcon(context.applicationInfo.icon)
        }
    }

    private fun setCallerColor(ringerData: RingerData, builder: NotificationCompat.Builder) {
        if (ringerData.notificationColor != null) {
            builder.color = Color.parseColor(ringerData.notificationColor)
        }
    }

    private fun setReminderColor(reminderData: ReminderData, builder: NotificationCompat.Builder) {
        if (reminderData.notificationColor != null) {
            builder.color = Color.parseColor(reminderData.notificationColor)
        }
    }

    private fun setSound(builder: NotificationCompat.Builder) {
        // get ringtone
        val ringtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        // set builder sound
        builder.setSound(ringtone)
    }

    private fun setCallerFullScreenIntent(
        ringerData: RingerData, builder: NotificationCompat.Builder
    ) {
        val intent = Intent(context, CallerActivity::class.java).apply {
            putExtras(ringerData.toBundle())
        }
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        } else {
            @Suppress("UnspecifiedImmutableFlag") PendingIntent.getActivity(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        builder.setFullScreenIntent(pendingIntent, true)
    }

    private fun setReminderFullScreenIntent(
        reminderData: ReminderData, builder: NotificationCompat.Builder
    ) {
        val intent = Intent(context, ReminderActivity::class.java).apply {
            putExtras(reminderData.toBundle())
        }
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        } else {
            @Suppress("UnspecifiedImmutableFlag") PendingIntent.getActivity(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        builder.setFullScreenIntent(pendingIntent, true)
    }

    private fun setCallerDeleteIntent(ringerData: RingerData, builder: NotificationCompat.Builder) {
        val intent = Intent(context, ActionBroadcastReceiver::class.java).apply {
            action = Definitions.ACTION_CALL_TIMED_OUT
            putExtras(ringerData.toBundle())
        }
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        } else {
            @Suppress("UnspecifiedImmutableFlag") PendingIntent.getBroadcast(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        builder.setDeleteIntent(pendingIntent)
    }

    private fun setReminderDeleteIntent(
        reminderData: ReminderData, builder: NotificationCompat.Builder
    ) {
        val intent = Intent(context, ActionBroadcastReceiver::class.java).apply {
            action = Definitions.ACTION_REMINDER_TIMED_OUT
            putExtras(reminderData.toBundle())
        }
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        } else {
            @Suppress("UnspecifiedImmutableFlag") PendingIntent.getBroadcast(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        builder.setDeleteIntent(pendingIntent)
    }

    private fun addCallAcceptAction(ringerData: RingerData, builder: NotificationCompat.Builder) {
//        val intent = Intent(context, ActionBroadcastReceiver::class.java).apply {
//            action = Definitions.ACTION_CALL_ACCEPT
//            putExtras(ringerData.toBundle())
//        }
        val launchIntent = IntentHelper.getLaunchIntent(context) ?: return
        // add data
        launchIntent.putExtra(Definitions.EXTRA_LAUNCHED_ON_CALL, true)
        launchIntent.putExtra(Definitions.EXTRA_LAUNCHED_ON_CALL_DATA, ringerData.toBundle())
        launchIntent.putExtra(
            Definitions.EXTRA_LAUNCHED_ACTION, Definitions.EXTRA_ACTION_CALL_ACCEPT
        )
        // create pending intent
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getActivity(
                context,
                0,
                launchIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        } else {
            @Suppress("UnspecifiedImmutableFlag") PendingIntent.getActivity(
                context, 0, launchIntent, PendingIntent.FLAG_UPDATE_CURRENT
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

    private fun addReminderAcceptAction(
        reminderData: ReminderData, builder: NotificationCompat.Builder
    ) {
        val launchIntent = IntentHelper.getLaunchIntent(context) ?: return
        // add data
        launchIntent.putExtra(Definitions.EXTRA_LAUNCHED_ON_REMINDER, true)
        launchIntent.putExtra(Definitions.EXTRA_LAUNCHED_ON_REMINDER_DATA, reminderData.toBundle())
        launchIntent.putExtra(
            Definitions.EXTRA_LAUNCHED_ACTION, Definitions.EXTRA_ACTION_REMINDER_ACCEPT
        )
        // create pending intent
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getActivity(
                context,
                0,
                launchIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        } else {
            @Suppress("UnspecifiedImmutableFlag") PendingIntent.getActivity(
                context, 0, launchIntent, PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        val action = NotificationCompat.Action.Builder(
            android.R.drawable.ic_menu_call,
            StringHelper.getColorizedText("Start", "#4CB050"),
            pendingIntent
        ).build()
        // add action to the notification builder
        builder.addAction(action)
    }

    private fun addCallRejectAction(ringerData: RingerData, builder: NotificationCompat.Builder) {
        val intent = Intent(context, ActionBroadcastReceiver::class.java).apply {
            action = Definitions.ACTION_CALL_REJECT
            putExtras(ringerData.toBundle())
        }
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        } else {
            @Suppress("UnspecifiedImmutableFlag") PendingIntent.getBroadcast(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
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

    private fun addReminderRejectAction(
        reminderData: ReminderData, builder: NotificationCompat.Builder
    ) {
        val intent = Intent(context, ActionBroadcastReceiver::class.java).apply {
            action = Definitions.ACTION_REMINDER_REJECT
            putExtras(reminderData.toBundle())
        }
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        } else {
            @Suppress("UnspecifiedImmutableFlag") PendingIntent.getBroadcast(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
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

    fun showMissedCallNotification(ringerData: RingerData) {
        // create builder
        val builder = NotificationCompat.Builder(context, ringerData.missedCallChannelId)
            .setContentTitle(ringerData.missedCallTitle)
            .setContentText(ringerData.missedCallDescription)
            .setSubText(ringerData.missedCallSubText).setPriority(NotificationCompat.PRIORITY_MAX)
            .setSmallIcon(android.R.drawable.stat_notify_missed_call).setColor(Color.RED)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setCategory(NotificationCompat.CATEGORY_MISSED_CALL)
        // show notification
        with(NotificationManagerCompat.from(context)) {
            notify(ringerData.callerId.hashCode(), builder.build())
        }
    }

    fun showMissedReminderNotification(reminderData: ReminderData) {
        // create builder
        val builder = NotificationCompat.Builder(context, reminderData.missedReminderChannelId)
            .setContentTitle(reminderData.missedReminderTitle)
            .setContentText(reminderData.missedReminderDescription)
            .setSubText(reminderData.missedReminderSubText)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setSmallIcon(android.R.drawable.stat_notify_missed_call).setColor(Color.RED)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setCategory(NotificationCompat.CATEGORY_MISSED_CALL)
        // show notification
        with(NotificationManagerCompat.from(context)) {
            notify(reminderData.reminderId.hashCode(), builder.build())
        }
    }
}
