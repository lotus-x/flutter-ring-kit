class AndroidNotificationChannelData {
  final String ringerChannelId;
  final String ringerChannelName;
  final String ringerChannelDescription;
  final String missedCallChannelId;
  final String missedCallChannelName;
  final String missedCallChannelDescription;

  final String reminderChannelId;
  final String reminderChannelName;
  final String reminderChannelDescription;
  final String missedReminderChannelId;
  final String missedReminderChannelName;
  final String missedReminderChannelDescription;

  AndroidNotificationChannelData({
    required this.ringerChannelId,
    required this.ringerChannelName,
    required this.ringerChannelDescription,
    required this.missedCallChannelId,
    required this.missedCallChannelName,
    required this.missedCallChannelDescription,
    required this.reminderChannelId,
    required this.reminderChannelName,
    required this.reminderChannelDescription,
    required this.missedReminderChannelId,
    required this.missedReminderChannelName,
    required this.missedReminderChannelDescription,
  });
}
