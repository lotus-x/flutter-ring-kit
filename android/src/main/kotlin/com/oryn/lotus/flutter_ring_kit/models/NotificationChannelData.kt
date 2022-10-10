package com.oryn.lotus.flutter_ring_kit.models

data class NotificationChannelData(val args: HashMap<String, Any?>) {
    val ringerChannelId: String = args["ringerChannelId"] as String
    val ringerChannelName: String = args["ringerChannelName"] as String
    val ringerChannelDescription: String = args["ringerChannelDescription"] as String
    val missedCallChannelId: String = args["missedCallChannelId"] as String
    val missedCallChannelName: String = args["missedCallChannelName"] as String
    val missedCallChannelDescription: String = args["missedCallChannelDescription"] as String

    val reminderChannelId: String = args["reminderChannelId"] as String
    val reminderChannelName: String = args["reminderChannelName"] as String
    val reminderChannelDescription: String = args["reminderChannelDescription"] as String
    val missedReminderChannelId: String = args["missedReminderChannelId"] as String
    val missedReminderChannelName: String = args["missedReminderChannelName"] as String
    val missedReminderChannelDescription: String =
        args["missedReminderChannelDescription"] as String
}
