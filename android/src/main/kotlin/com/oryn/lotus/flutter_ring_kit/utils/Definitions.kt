package com.oryn.lotus.flutter_ring_kit.utils

class Definitions {
    companion object {
        // intent actions
        const val ACTION_CALL_ACCEPT = "com.oryn.lotus.flutter_ring_kit.ACTION_CALL_ANSWER"
        const val ACTION_CALL_REJECT = "com.oryn.lotus.flutter_ring_kit.ACTION_CALL_REJECT"
        const val ACTION_CALL_TIMED_OUT = "com.oryn.lotus.flutter_ring_kit.ACTION_CALL_TIMED_OUT"

        const val ACTION_REMINDER_ACCEPT = "com.oryn.lotus.flutter_ring_kit.ACTION_REMINDER_ANSWER"
        const val ACTION_REMINDER_REJECT = "com.oryn.lotus.flutter_ring_kit.ACTION_REMINDER_REJECT"
        const val ACTION_REMINDER_TIMED_OUT =
            "com.oryn.lotus.flutter_ring_kit.ACTION_REMINDER_TIMED_OUT"

        // extras
        const val EXTRA_LAUNCHED_ON_CALL = "com.oryn.lotus.flutter_ring_kit.EXTRA_LAUNCHED_ON_CALL"
        const val EXTRA_LAUNCHED_ON_CALL_DATA =
            "com.oryn.lotus.flutter_ring_kit.EXTRA_LAUNCHED_ON_CALL_DATA"
        const val EXTRA_LAUNCHED_ACTION = "com.oryn.lotus.flutter_ring_kit.EXTRA_LAUNCHED_ACTION"
        const val EXTRA_ACTION_CALL_ACCEPT = "action_call_answer"
        const val EXTRA_CALLER_ID = "caller_id"
        const val EXTRA_CALLER_NAME = "caller_name"
        const val EXTRA_CALLER_GENDER = "caller_gender"
        const val EXTRA_CALLER_IMAGE_URL = "caller_image_url"
        const val EXTRA_FULLSCREEN_TITLE = "fullscreen_title"
        const val EXTRA_FULLSCREEN_DESCRIPTION = "fullscreen_description"
        const val EXTRA_MISSED_CALL_CHANNEL_ID = "missed_call_channel_id"
        const val EXTRA_MISSED_CALL_TITLE = "missed_call_channel_title"
        const val EXTRA_MISSED_CALL_DESCRIPTION = "missed_call_channel_description"
        const val EXTRA_MISSED_CALL_SUB_TEXT = "missed_call_channel_sub_text"

        const val EXTRA_LAUNCHED_ON_REMINDER =
            "com.oryn.lotus.flutter_ring_kit.EXTRA_LAUNCHED_ON_REMINDER"
        const val EXTRA_LAUNCHED_ON_REMINDER_DATA =
            "com.oryn.lotus.flutter_ring_kit.EXTRA_LAUNCHED_ON_REMINDER_DATA"
        const val EXTRA_ACTION_REMINDER_ACCEPT = "action_reminder_answer"
        const val EXTRA_REMINDER_ID = "reminder_id"
        const val EXTRA_REMINDER_NAME = "reminder_name"
        const val EXTRA_MISSED_REMINDER_CHANNEL_ID = "missed_reminder_channel_id"
        const val EXTRA_MISSED_REMINDER_TITLE = "missed_reminder_channel_title"
        const val EXTRA_MISSED_REMINDER_DESCRIPTION = "missed_reminder_channel_description"
        const val EXTRA_MISSED_REMINDER_SUB_TEXT = "missed_reminder_channel_sub_text"
    }
}
