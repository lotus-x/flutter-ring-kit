package com.oryn.lotus.flutter_ring_kit

import android.content.Context
import android.content.Intent
import androidx.annotation.NonNull
import androidx.core.app.NotificationManagerCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
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
        checkLaunchedOnCallAction(binding.activity.intent)
    }

    override fun onDetachedFromActivityForConfigChanges() {}

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        binding.addOnNewIntentListener(this)
        // check launched when tap caller notification
        checkLaunchedOnCallAction(binding.activity.intent)
    }

    override fun onDetachedFromActivity() {}

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        if (this.context == null) {
            result.notImplemented()
            return
        }
        when (call.method) {
            "createNotificationChannel" -> {
                // get arguments
                val arguments = call.arguments<HashMap<String, Any?>>() ?: return
                // extract data
                val data = NotificationChannelData(arguments)
                // create notification builder
                val notificationBuilder = NotificationBuilder(this.context!!)
                notificationBuilder.createNotificationChannels(data)
                result.success(true)
            }
            "showCallNotification" -> {
                // get arguments
                val arguments = call.arguments<HashMap<String, Any?>>() ?: return
                // extract data
                val data = RingerData(arguments)
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

    override fun onNewIntent(intent: Intent): Boolean {
        return checkLaunchedOnCallAction(intent)
    }

    private fun checkLaunchedOnCallAction(intent: Intent?): Boolean {
        // check null
        if (intent == null) return false
        // check launch intent
        if (intent.action == Intent.ACTION_MAIN && intent.hasCategory(Intent.CATEGORY_LAUNCHER)) {
            // get extra
            val launchedOnCall = intent.getBooleanExtra(
                Definitions.EXTRA_LAUNCHED_ON_CALL,
                false
            )
            if (launchedOnCall) {
                val launchedData = intent.getBundleExtra(
                    Definitions.EXTRA_LAUNCHED_ON_CALL_DATA
                ) ?: return false
                // create ringer data
                val ringerData = RingerData.fromBundle(launchedData)
                // create local intent
                var localIntent: Intent? = null
                // get call action
                val callAction = intent.getStringExtra(Definitions.EXTRA_LAUNCHED_ACTION)
                // check action
                if (callAction == Definitions.EXTRA_ACTION_CALL_ACCEPT) {
                    // close notification
                    if (context != null) {
                        with(NotificationManagerCompat.from(context!!)) {
                            cancel(ringerData.callerId.hashCode())
                        }
                    }
                    // save caller details
                    this.launchedOnCall = true
                    this.launchedCallerId = ringerData.callerId
                    // create local intent
                    localIntent = Intent(Definitions.ACTION_CALL_ACCEPT).apply {
                        putExtras(ringerData.toBundle())
                    }
                }
                // send local broadcast intent
                if (localIntent != null && context != null) {
                    with(LocalBroadcastManager.getInstance(context!!.applicationContext)) {
                        sendBroadcast(localIntent)
                    }
                }
            }
        }
        return false
    }
}
