class AndroidReminderData {
  final String notificationChannelId;
  final String missedReminderChannelId;
  final Duration notificationTimeout;
  final String? notificationIcon;
  final String? notificationColor;
  final String notificationTitle;
  final String notificationDescription;
  final String fullScreenTitle;
  final String fullScreenDescription;
  final String missedReminderTitle;
  final String missedReminderDescription;
  final String missedReminderSubText;

  AndroidReminderData({
    required this.notificationChannelId,
    required this.missedReminderChannelId,
    this.notificationTimeout = const Duration(seconds: 60),
    this.notificationIcon,
    this.notificationColor,
    required this.notificationTitle,
    required this.notificationDescription,
    required this.fullScreenTitle,
    required this.fullScreenDescription,
    required this.missedReminderTitle,
    required this.missedReminderDescription,
    required this.missedReminderSubText,
  });
}
