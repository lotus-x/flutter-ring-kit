package com.oryn.lotus.flutter_ring_kit.models

data class NotificationChannelData(val args: HashMap<String, Any?>) {
    val ringerChannelId: String = args["ringerChannelId"] as String
    val ringerChannelName: String = args["ringerChannelName"] as String
    val ringerChannelDescription: String = args["ringerChannelDescription"] as String
    val missedCallChannelId: String = args["missedCallChannelId"] as String
    val missedCallChannelName: String = args["missedCallChannelName"] as String
    val missedCallChannelDescription: String = args["missedCallChannelDescription"] as String
}
