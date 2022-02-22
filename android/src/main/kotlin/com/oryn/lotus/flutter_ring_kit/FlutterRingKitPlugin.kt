package com.oryn.lotus.flutter_ring_kit

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.annotation.NonNull
import com.oryn.lotus.flutter_ring_kit.models.NotificationChannelData
import com.oryn.lotus.flutter_ring_kit.models.RingerData
import com.oryn.lotus.flutter_ring_kit.notifications.NotificationBuilder
import com.oryn.lotus.flutter_ring_kit.utils.Definitions

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry

class FlutterRingKitPlugin : FlutterPlugin, ActivityAware, MethodCallHandler,
    PluginRegistry.NewIntentListener {
    private lateinit var channel: MethodChannel
    private lateinit var callerEventChannel: EventChannel
    private var context: Context? = null

    private var launchedOnCall = false
    private var launchedCallerId = ""

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        this.context = flutterPluginBinding.applicationContext
        channel = MethodChannel(
            flutterPluginBinding.binaryMessenger,
            "com.oryn.lotus/flutter_ring_kit"
        )
        channel.setMethodCallHandler(this)
        callerEventChannel = EventChannel(
            flutterPluginBinding.binaryMessenger,
            "com.oryn.lotus/flutter_ring_kit/caller_callback"
        )
        callerEventChannel.setStreamHandler(
            CallEventHandler(flutterPluginBinding.applicationContext)
        )
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        this.context = null
        channel.setMethodCallHandler(null)
        callerEventChannel.setStreamHandler(null)
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        binding.addOnNewIntentListener(this)
        // check launched when tap caller notification
        checkLaunchedOnCallAction(binding.activity)
    }

    override fun onDetachedFromActivityForConfigChanges() {}

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        binding.addOnNewIntentListener(this)
        // check launched when tap caller notification
        checkLaunchedOnCallAction(binding.activity)
    }

    override fun onDetachedFromActivity() {}

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        if (this.context == null) {
            result.notImplemented()
            return
        }
        when (call.method) {
            "createNotificationChannel" -> {
                // extract data
                val data = NotificationChannelData(call.arguments())
                // create notification builder
                val notificationBuilder = NotificationBuilder(this.context!!)
                notificationBuilder.createNotificationChannels(data)
                result.success(true)
            }
            "showCallNotification" -> {
                // extract data
                val data = RingerData(call.arguments())
                // create notification builder
                val notificationBuilder = NotificationBuilder(this.context!!)
                notificationBuilder.showRingNotification(data)
                result.success(true)
            }
            "checkLaunchedUponCall" -> {
                result.success(launchedOnCall)
            }
            "getLaunchedCallerId" -> {
                result.success(launchedCallerId)
            }
            else -> result.notImplemented()
        }
    }

    override fun onNewIntent(intent: Intent?): Boolean {
        // check null
        if (intent == null) return false
        // check launch intent
        if (intent.action == Intent.ACTION_MAIN && intent.hasCategory(Intent.CATEGORY_LAUNCHER)) {
            // get extra
            val actionExtra = intent.getStringExtra(
                Definitions.EXTRA_LAUNCHED_ACTION
            ) ?: return false
            // check type
            when (actionExtra) {
                Definitions.EXTRA_ACTION_CALL_ACCEPT -> {

                }
            }
        }
        return false
    }

    private fun checkLaunchedOnCallAction(activity: Activity) {
        // get intent
        val launchedIntent = activity.intent
        // check action
        val actionExtra = launchedIntent.getStringExtra(Definitions.EXTRA_LAUNCHED_ACTION) ?: return
        // check accept or reject
        if (actionExtra == Definitions.EXTRA_ACTION_CALL_ACCEPT) {
            launchedOnCall = true
            launchedCallerId = launchedIntent.getStringExtra(Definitions.EXTRA_CALLER_ID) ?: ""
        }
    }
}
