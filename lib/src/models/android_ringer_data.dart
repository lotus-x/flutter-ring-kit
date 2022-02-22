class AndroidRingerData {
  final String notificationChannelId;
  final String missedCallChannelId;
  final Duration notificationTimeout;
  final String? notificationIcon;
  final String? notificationColor;
  final String notificationTitle;
  final String notificationDescription;
  final String fullScreenTitle;
  final String fullScreenDescription;
  final String missedCallTitle;
  final String missedCallDescription;
  final String missedCallSubText;

  AndroidRingerData({
    required this.notificationChannelId,
    required this.missedCallChannelId,
    this.notificationTimeout = const Duration(seconds: 60),
    this.notificationIcon,
    this.notificationColor,
    required this.notificationTitle,
    required this.notificationDescription,
    required this.fullScreenTitle,
    required this.fullScreenDescription,
    required this.missedCallTitle,
    required this.missedCallDescription,
    required this.missedCallSubText,
  });
}
