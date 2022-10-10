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
  String launchedCallerId = "";
  bool launchedUponReminder = false;
  String launchedReminderId = "";

  @override
  void initState() {
    super.initState();
    initFirebaseMessaging();
    initFlutterRingKit();
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

      // ring();
      remind();
    });

    FirebaseMessaging.onBackgroundMessage(_firebaseMessagingBackgroundHandler);

    debugPrint(await FirebaseMessaging.instance.getToken());
  }

  Future<void> initFlutterRingKit() async {
    // create ring kit
    final ringKit = FlutterRingKit();
    // initialize
    await ringKit.init(
      onCallAnswer: (callerId) {
        print(callerId);
      },
      onReminderAccept: (reminderId) {
        print(reminderId);
      },
      androidNotificationChannelData: AndroidNotificationChannelData(
        ringerChannelId: "flutter_ring_kit_calls",
        ringerChannelName: "Flutter Ring Kit Calls",
        ringerChannelDescription: "Flutter Ring Kit Testing",
        missedCallChannelId: "flutter_ring_kit_missed_calls",
        missedCallChannelName: "Flutter Ring Kit Missed Calls",
        missedCallChannelDescription: "Flutter Ring Kit Testing",
        reminderChannelId: "flutter_ring_kit_reminders",
        reminderChannelName: "Flutter Ring Kit Reminders",
        reminderChannelDescription: "Flutter Ring Kit Testing",
        missedReminderChannelId: "flutter_ring_kit_missed_reminders",
        missedReminderChannelName: "Flutter Ring Kit Missed Reminders",
        missedReminderChannelDescription: "Flutter Ring Kit Testing",
      ),
    );
    final launchedOnCall = await ringKit.launchedUponCall();
    String _launchedCallerId = "";
    if (launchedOnCall) {
      _launchedCallerId = await ringKit.launchedCallerId();
    }
    setState(() {
      launchedUponCall = launchedOnCall;
      launchedCallerId = _launchedCallerId;
    });
    final launchedOnReminder = await ringKit.launchedUponReminder();
    String _launchedReminderId = "";
    if (launchedOnReminder) {
      _launchedReminderId = await ringKit.launchedReminderId();
    }
    setState(() {
      launchedUponReminder = launchedOnReminder;
      launchedReminderId = _launchedReminderId;
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
                onPressed: () => ring(),
                child: const Text("Show Notification Call"),
              ),
            ),
            Center(
              child: TextButton(
                onPressed: () async {
                  await Future.delayed(const Duration(seconds: 2));
                  ring();
                },
                child: const Text("Show Notification Call After Delay"),
              ),
            ),
            Center(
              child: TextButton(
                onPressed: () => remind(),
                child: const Text("Show Notification Reminder"),
              ),
            ),
            Center(
              child: TextButton(
                onPressed: () async {
                  await Future.delayed(const Duration(seconds: 2));
                  remind();
                },
                child: const Text("Show Notification Reminder After Delay"),
              ),
            ),
            const SizedBox(height: 20),
            Center(
              child: Text("launched upon call: $launchedUponCall"),
            ),
            Center(
              child: Text("launched caller ID: $launchedCallerId"),
            ),
            const SizedBox(height: 20),
            Center(
              child: Text("launched upon reminder: $launchedUponReminder"),
            ),
            Center(
              child: Text("launched reminder ID: $launchedReminderId"),
            )
          ],
        ),
      ),
    );
  }
}

Future<void> _firebaseMessagingBackgroundHandler(RemoteMessage message) async {
  debugPrint("Handling a background message: ${message.data}");
  // ring();
  remind();
}

Future<void> ring() async {
  // create ring kit
  final ringKit = FlutterRingKit();
  // ring
  await ringKit.ring(
    callerId: "1234",
    callerName: "Dr. Megahead Pillow",
    // callerGender: false,
    // callerImageUrl:
    //     "https://www.rd.com/wp-content/uploads/2017/09/01-shutterstock_476340928-Irina-Bg.jpg?resize=760,506",
    android: AndroidRingerData(
      notificationChannelId: "flutter_ring_kit_calls",
      missedCallChannelId: "flutter_ring_kit_missed_calls",
      notificationTimeout: const Duration(seconds: 10),
      notificationIcon: "sample_icon",
      notificationColor: "#00e047",
      notificationTitle: "Incoming Video Consultation",
      notificationDescription:
          "Dr. Megahead Pillow, Appointment Number #3, test test test test",
      fullScreenTitle: "Incoming Consultation",
      fullScreenDescription: "Appointment Number #1",
      missedCallTitle: "Missed Consultation",
      missedCallDescription: "Dr. Megahead Pillow, Appointment Number #3",
      missedCallSubText: "Missed Consultation",
    ),
  );
}

Future<void> remind() async {
  // create ring kit
  final ringKit = FlutterRingKit();
  // ring
  await ringKit.remind(
    reminderId: "56789",
    reminderName: "Dr. Maureen Biologist",
    android: AndroidReminderData(
      notificationChannelId: "flutter_ring_kit_reminders",
      missedReminderChannelId: "flutter_ring_kit_missed_reminders",
      notificationTimeout: const Duration(seconds: 10),
      notificationIcon: "sample_icon",
      notificationColor: "#00e047",
      notificationTitle: "Video Consultation Ready",
      notificationDescription: "Dr. Megahead Pillow, 10th of October 2022",
      fullScreenTitle: "Consultation Ready",
      fullScreenDescription: "10th of October 2022",
      missedReminderTitle: "Missed Consultation",
      missedReminderDescription: "Dr. Megahead Pillow, 10th of October 2022",
      missedReminderSubText: "Missed Consultation",
    ),
  );
}
