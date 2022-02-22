import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'package:flutter_ring_kit/src/models/android_notification_channel_data.dart';
import 'package:flutter_ring_kit/src/models/android_ringer_data.dart';

// ignore: avoid_classes_with_only_static_members
class FlutterRingKit {
  final _channel = const MethodChannel('com.oryn.lotus/flutter_ring_kit');

  Future<void> init({
    required AndroidNotificationChannelData androidNotificationChannelData,
  }) async {
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
        },
      );
    }
  }

  Future<void> ring({
    required String callerId,
    required String callerName,
    bool callerGender = true,
    String? callerImageUrl,
    required AndroidRingerData android,
  }) async {
    try {
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
    } catch (e) {
      print(e);
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
}
