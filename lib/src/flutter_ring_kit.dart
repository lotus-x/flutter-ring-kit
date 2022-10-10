import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'package:flutter_ring_kit/src/models/android_notification_channel_data.dart';
import 'package:flutter_ring_kit/src/models/android_reminder_data.dart';
import 'package:flutter_ring_kit/src/models/android_ringer_data.dart';

typedef CallAnsweredCallBack = void Function(String callerId);
typedef ReminderAcceptedCallBack = void Function(String reminderId);

class FlutterRingKit {
  final _channel = const MethodChannel('com.oryn.lotus/flutter_ring_kit');
  final _callerEventChannel = const EventChannel(
    'com.oryn.lotus/flutter_ring_kit/caller_callback',
  );

  CallAnsweredCallBack? _onCallAnswer;
  ReminderAcceptedCallBack? _onReminderAccept;

  Future<void> init({
    required AndroidNotificationChannelData androidNotificationChannelData,
    CallAnsweredCallBack? onCallAnswer,
    ReminderAcceptedCallBack? onReminderAccept,
  }) async {
    // save call backs
    _onCallAnswer = onCallAnswer;
    _onReminderAccept = onReminderAccept;
    // check platform
    if (defaultTargetPlatform == TargetPlatform.android) {
      // create notification channel
      await _channel.invokeMethod(
        "createNotificationChannel",
        {
          "ringerChannelId": androidNotificationChannelData.ringerChannelId,
          "ringerChannelName": androidNotificationChannelData.ringerChannelName,
          "ringerChannelDescription":
              androidNotificationChannelData.ringerChannelDescription,
          "missedCallChannelId":
              androidNotificationChannelData.missedCallChannelId,
          "missedCallChannelName":
              androidNotificationChannelData.missedCallChannelName,
          "missedCallChannelDescription":
              androidNotificationChannelData.missedCallChannelDescription,
          "reminderChannelId": androidNotificationChannelData.reminderChannelId,
          "reminderChannelName":
              androidNotificationChannelData.reminderChannelName,
          "reminderChannelDescription":
              androidNotificationChannelData.reminderChannelDescription,
          "missedReminderChannelId":
              androidNotificationChannelData.missedReminderChannelId,
          "missedReminderChannelName":
              androidNotificationChannelData.missedReminderChannelName,
          "missedReminderChannelDescription":
              androidNotificationChannelData.missedReminderChannelDescription,
        },
      );
    }
    // listen to events
    listenCallerEvents();
  }

  Future<void> ring({
    required String callerId,
    required String callerName,
    bool callerGender = true,
    String? callerImageUrl,
    required AndroidRingerData android,
  }) async {
    // check platform
    switch (defaultTargetPlatform) {
      case TargetPlatform.android:
        await _channel.invokeMethod(
          "showCallNotification",
          {
            "callerId": callerId,
            "callerName": callerName,
            "callerGender": callerGender,
            "callerImageUrl": callerImageUrl,
            "notificationChannelId": android.notificationChannelId,
            "missedCallChannelId": android.missedCallChannelId,
            "notificationTimeout": android.notificationTimeout.inMilliseconds,
            "notificationIcon": android.notificationIcon,
            "notificationColor": android.notificationColor,
            "notificationTitle": android.notificationTitle,
            "notificationDescription": android.notificationDescription,
            "fullScreenTitle": android.fullScreenTitle,
            "fullScreenDescription": android.fullScreenDescription,
            "missedCallTitle": android.missedCallTitle,
            "missedCallDescription": android.missedCallDescription,
            "missedCallSubText": android.missedCallSubText,
          },
        );
        break;
      case TargetPlatform.iOS:
        await _channel.invokeMethod("ringCall");
        break;
      default:
    }
  }

  Future<void> remind({
    required String reminderId,
    required String reminderName,
    required AndroidReminderData android,
  }) async {
    // check platform
    switch (defaultTargetPlatform) {
      case TargetPlatform.android:
        await _channel.invokeMethod(
          "showReminderNotification",
          {
            "reminderId": reminderId,
            "reminderName": reminderName,
            "notificationChannelId": android.notificationChannelId,
            "missedReminderChannelId": android.missedReminderChannelId,
            "notificationTimeout": android.notificationTimeout.inMilliseconds,
            "notificationIcon": android.notificationIcon,
            "notificationColor": android.notificationColor,
            "notificationTitle": android.notificationTitle,
            "notificationDescription": android.notificationDescription,
            "fullScreenTitle": android.fullScreenTitle,
            "fullScreenDescription": android.fullScreenDescription,
            "missedReminderTitle": android.missedReminderTitle,
            "missedReminderDescription": android.missedReminderDescription,
            "missedReminderSubText": android.missedReminderSubText,
          },
        );
        break;
      case TargetPlatform.iOS:
        await _channel.invokeMethod("ringReminder");
        break;
      default:
    }
  }

  Future<bool> launchedUponCall() async {
    final result = await _channel.invokeMethod("checkLaunchedUponCall");
    return result as bool;
  }

  Future<String> launchedCallerId() async {
    final result = await _channel.invokeMethod("getLaunchedCallerId");
    return result as String;
  }

  Future<bool> launchedUponReminder() async {
    final result = await _channel.invokeMethod("checkLaunchedUponReminder");
    return result as bool;
  }

  Future<String> launchedReminderId() async {
    final result = await _channel.invokeMethod("getLaunchedReminderId");
    return result as String;
  }

  void listenCallerEvents() {
    _callerEventChannel.receiveBroadcastStream().listen((event) {
      // check type
      switch (event['type']) {
        case "call_answered":
          // get caller id
          final callerId = event["callerId"] as String?;
          // check empty
          if (callerId == null) return;
          // invoke callback
          _onCallAnswer?.call(callerId);
          break;
        case "reminder_accepted":
          // get reminder id
          final reminderId = event["reminderId"] as String?;
          // check empty
          if (reminderId == null) return;
          // invoke callback
          _onReminderAccept?.call(reminderId);
          break;
        default:
      }
    });
  }
}
