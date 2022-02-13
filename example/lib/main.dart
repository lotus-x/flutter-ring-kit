import 'dart:async';
import 'package:firebase_core/firebase_core.dart';
import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter/material.dart';
import 'package:flutter_ring_kit/flutter_ring_kit.dart';
import 'package:flutter_ring_kit_example/firebase_options.dart';

Future<void> main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await Firebase.initializeApp(
    options: DefaultFirebaseOptions.currentPlatform,
  );
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  bool launchedUponCall = false;

  @override
  void initState() {
    super.initState();
    initFirebaseMessaging();
    checkLaunchedUponCall();
  }

  Future<void> initFirebaseMessaging() async {
    final messaging = FirebaseMessaging.instance;
    final settings = await messaging.requestPermission();
    debugPrint('User granted permission: ${settings.authorizationStatus}');

    FirebaseMessaging.onMessage.listen((RemoteMessage message) {
      debugPrint('Got a message whilst in the foreground!');
      debugPrint('Message data: ${message.data}');

      if (message.notification != null) {
        debugPrint(
          'Message also contained a notification: ${message.notification}',
        );
      }

      FlutterRingKit.ring();
    });

    FirebaseMessaging.onBackgroundMessage(_firebaseMessagingBackgroundHandler);

    debugPrint(await FirebaseMessaging.instance.getToken());
  }

  Future<void> checkLaunchedUponCall() async {
    final launchedOnCall = await FlutterRingKit.launchedUponCall();
    setState(() {
      launchedUponCall = launchedOnCall;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Center(
              child: TextButton(
                onPressed: () => FlutterRingKit.ring(),
                child: const Text("Show Notification Call"),
              ),
            ),
            const SizedBox(height: 20),
            Center(
              child: Text("launched upon call: $launchedUponCall"),
            )
          ],
        ),
      ),
    );
  }
}

Future<void> _firebaseMessagingBackgroundHandler(RemoteMessage message) async {
  debugPrint("Handling a background message: ${message.data}");
  FlutterRingKit.ring();
}
