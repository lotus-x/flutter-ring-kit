package com.oryn.lotus.flutter_ring_kit.models

import android.os.Bundle
import com.oryn.lotus.flutter_ring_kit.utils.Definitions

data class ReminderData(val args: HashMap<String, Any?>) {
    val reminderId: String = args["reminderId"] as String
    val reminderName: String = args["reminderName"] as String
    val notificationChannelId: String = args["notificationChannelId"] as String
    val missedReminderChannelId: String = args["missedReminderChannelId"] as String
    val notificationTimeout: Int = args["notificationTimeout"] as Int
    val notificationIcon: String? = args["notificationIcon"] as String?
    val notificationColor: String? = args["notificationColor"] as String?
    val notificationTitle: String = args["notificationTitle"] as String
    val notificationDescription: String = args["notificationDescription"] as String
    val fullScreenTitle: String = args["fullScreenTitle"] as String
    val fullScreenDescription: String = args["fullScreenDescription"] as String
    val missedReminderTitle: String = args["missedReminderTitle"] as String
    val missedReminderDescription: String = args["missedReminderDescription"] as String
    val missedReminderSubText: String = args["missedReminderSubText"] as String

    companion object {
        fun fromBundle(bundle: Bundle): ReminderData {
            // create map
            val dataMap = HashMap<String, Any?>()
            // add data
            dataMap["reminderId"] = bundle.getString(Definitions.EXTRA_REMINDER_ID, "")
            dataMap["reminderName"] = bundle.getString(Definitions.EXTRA_REMINDER_NAME, "")
            dataMap["notificationChannelId"] = ""
            dataMap["missedReminderChannelId"] = bundle.getString(
                Definitions.EXTRA_MISSED_REMINDER_CHANNEL_ID,
                ""
            )
            dataMap["notificationTimeout"] = 60000
            dataMap["notificationIcon"] = null
            dataMap["notificationColor"] = null
            dataMap["notificationTitle"] = ""
            dataMap["notificationDescription"] = ""
            dataMap["fullScreenTitle"] = bundle.getString(
                Definitions.EXTRA_FULLSCREEN_TITLE,
                ""
            )
            dataMap["fullScreenDescription"] = bundle.getString(
                Definitions.EXTRA_FULLSCREEN_DESCRIPTION,
                ""
            )
            dataMap["missedReminderTitle"] = bundle.getString(
                Definitions.EXTRA_MISSED_REMINDER_TITLE,
                ""
            )
            dataMap["missedReminderDescription"] = bundle.getString(
                Definitions.EXTRA_MISSED_REMINDER_DESCRIPTION,
                ""
            )
            dataMap["missedReminderSubText"] = bundle.getString(
                Definitions.EXTRA_MISSED_REMINDER_SUB_TEXT,
                ""
            )
            // create data from map
            return ReminderData(dataMap)
        }
    }

    fun toBundle(): Bundle {
        val bundle = Bundle()
        bundle.putString(Definitions.EXTRA_REMINDER_ID, reminderId)
        bundle.putString(Definitions.EXTRA_REMINDER_NAME, reminderName)
        bundle.putString(Definitions.EXTRA_FULLSCREEN_TITLE, fullScreenTitle)
        bundle.putString(Definitions.EXTRA_FULLSCREEN_DESCRIPTION, fullScreenDescription)
        bundle.putString(Definitions.EXTRA_MISSED_REMINDER_CHANNEL_ID, missedReminderChannelId)
        bundle.putString(Definitions.EXTRA_MISSED_REMINDER_TITLE, missedReminderTitle)
        bundle.putString(Definitions.EXTRA_MISSED_REMINDER_DESCRIPTION, missedReminderDescription)
        bundle.putString(Definitions.EXTRA_MISSED_REMINDER_SUB_TEXT, missedReminderSubText)
        return bundle
    }
}