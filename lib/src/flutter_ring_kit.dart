import 'package:flutter/services.dart';

// ignore: avoid_classes_with_only_static_members
class FlutterRingKit {
  static const _channel = MethodChannel('com.oryn.lotus/flutter_ring_kit');

  static Future<void> ring() async {
    try {
      final result = await _channel.invokeMethod("showCallNotification");
      print(result);
    } catch (e) {
      print(e);
    }
  }

  static Future<bool> launchedUponCall() async {
    final result = await _channel.invokeMethod("checkLaunchedUponCall");
    return result as bool;
  }
}
