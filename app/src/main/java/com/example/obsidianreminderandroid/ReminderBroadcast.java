package com.example.obsidianreminderandroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ReminderBroadcast extends BroadcastReceiver {
  static String NOTIFICATION_CHANNEL_ID = "OBSIDIAN_TASK_NOTIFICATIONS_ID";
  static String NOTIFICATION_CHANNEL = "OBSIDIAN_TASK_NOTIFICATIONS";
  @Override
  public void onReceive(Context context, Intent intent) {
    NotificationCompat.Builder builder = new NotificationCompat.Builder(context,NOTIFICATION_CHANNEL_ID)
      .setSmallIcon(R.drawable.ic_launcher_background)
      .setContentTitle("File changed!")
      .setContentText(intent.getStringExtra(NOTIFICATION_CHANNEL))
      .setPriority(NotificationCompat.PRIORITY_DEFAULT);

    NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
    notificationManagerCompat.notify(intent.getIntExtra(NOTIFICATION_CHANNEL_ID, 42), builder.build());
  }
}
