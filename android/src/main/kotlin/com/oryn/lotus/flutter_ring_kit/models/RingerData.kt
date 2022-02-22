package com.oryn.lotus.flutter_ring_kit.models

import android.os.Bundle
import com.oryn.lotus.flutter_ring_kit.utils.Definitions

data class RingerData(val args: HashMap<String, Any?>) {
    val callerId: String = args["callerId"] as String
    val callerName: String = args["callerName"] as String
    val callerGender: Boolean = args["callerGender"] as Boolean
    val callerImageUrl: String? = args["callerImageUrl"] as String?
    val notificationChannelId: String = args["notificationChannelId"] as String
    val missedCallChannelId: String = args["missedCallChannelId"] as String
    val notificationTimeout: Int = args["notificationTimeout"] as Int
    val notificationIcon: String? = args["notificationIcon"] as String?
    val notificationColor: String? = args["notificationColor"] as String?
    val notificationTitle: String = args["notificationTitle"] as String
    val notificationDescription: String = args["notificationDescription"] as String
    val fullScreenTitle: String = args["fullScreenTitle"] as String
    val fullScreenDescription: String = args["fullScreenDescription"] as String
    val missedCallTitle: String = args["missedCallTitle"] as String
    val missedCallDescription: String = args["missedCallDescription"] as String
    val missedCallSubText: String = args["missedCallSubText"] as String

    companion object {
        fun fromBundle(bundle: Bundle): RingerData {
            // create map
            val dataMap = HashMap<String, Any?>()
            // add data
            dataMap["callerId"] = bundle.getString(Definitions.EXTRA_CALLER_ID, "")
            dataMap["callerName"] = bundle.getString(Definitions.EXTRA_CALLER_NAME, "")
            dataMap["callerGender"] = bundle.getBoolean(
                Definitions.EXTRA_CALLER_GENDER,
                true
            )
            dataMap["callerImageUrl"] = bundle.getString(
                Definitions.EXTRA_CALLER_IMAGE_URL,
                ""
            ).ifEmpty { null }
            dataMap["notificationChannelId"] = ""
            dataMap["missedCallChannelId"] = bundle.getString(
                Definitions.EXTRA_MISSED_CALL_CHANNEL_ID,
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
            dataMap["missedCallTitle"] = bundle.getString(
                Definitions.EXTRA_MISSED_CALL_TITLE,
                ""
            )
            dataMap["missedCallDescription"] = bundle.getString(
                Definitions.EXTRA_MISSED_CALL_DESCRIPTION,
                ""
            )
            dataMap["missedCallSubText"] = bundle.getString(
                Definitions.EXTRA_MISSED_CALL_SUB_TEXT,
                ""
            )
            // create data from map
            return RingerData(dataMap)
        }
    }

    fun toBundle(): Bundle {
        val bundle = Bundle()
        bundle.putString(Definitions.EXTRA_CALLER_ID, callerId)
        bundle.putString(Definitions.EXTRA_CALLER_NAME, callerName)
        bundle.putBoolean(Definitions.EXTRA_CALLER_GENDER, callerGender)
        bundle.putString(Definitions.EXTRA_CALLER_IMAGE_URL, callerImageUrl)
        bundle.putString(Definitions.EXTRA_FULLSCREEN_TITLE, fullScreenTitle)
        bundle.putString(Definitions.EXTRA_FULLSCREEN_DESCRIPTION, fullScreenDescription)
        bundle.putString(Definitions.EXTRA_MISSED_CALL_CHANNEL_ID, missedCallChannelId)
        bundle.putString(Definitions.EXTRA_MISSED_CALL_TITLE, missedCallTitle)
        bundle.putString(Definitions.EXTRA_MISSED_CALL_DESCRIPTION, missedCallDescription)
        bundle.putString(Definitions.EXTRA_MISSED_CALL_SUB_TEXT, missedCallSubText)
        return bundle
    }
}
