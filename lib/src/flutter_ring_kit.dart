import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

// ignore: avoid_classes_with_only_static_members
class FlutterRingKit {
  static const _channel = MethodChannel('com.oryn.lotus/flutter_ring_kit');

  static Future<void> ring() async {
    try {
      // check platform
      switch (defaultTargetPlatform) {
        case TargetPlatform.android:
          final result = await _channel.invokeMethod("showCallNotification");
          print(result);
          break;
        case TargetPlatform.iOS:
          final result = await _channel.invokeMethod("ringCall");
          print(result);
          break;
        default:
      }
    } catch (e) {
      print(e);
    }
  }

  static Future<bool> launchedUponCall() async {
    final result = await _channel.invokeMethod("checkLaunchedUponCall");
    return result as bool;
  }
}
