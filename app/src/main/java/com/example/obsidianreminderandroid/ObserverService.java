package com.example.obsidianreminderandroid;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ObserverService extends Service {
  public static final String CHANNEL_ID = "ForegroundServiceChannel";
  String NOTIFICATION_CHANNEL_ID = "OBSIDIAN_TASK_NOTIFICATIONS_ID";

  @Override
  public void onCreate() {
    super.onCreate();
  }
  @RequiresApi(api = Build.VERSION_CODES.O)
  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    Context context = this;
    String path = "";
    if (intent.hasExtra("dataUri")) {
      path = intent.getStringExtra("dataUri");
      ensureNotificationChannelExists();
      this.createForeGroundNotification(context);
      DirectoryFileObserver directoryFileObserver = new DirectoryFileObserver(path, context);
      directoryFileObserver.startWatching();
    }

    return START_NOT_STICKY;
  }

  private void createForeGroundNotification(Context context) {
    ensureNotificationChannelExists();
    NotificationCompat.Builder builder = new NotificationCompat.Builder(context,NOTIFICATION_CHANNEL_ID)
      .setSmallIcon(R.drawable.ic_launcher_background)
      .setContentTitle("Obsidian Reminder running")
      .setContentText("background")
      .setPriority(NotificationCompat.PRIORITY_DEFAULT);
    Notification notification = builder.build();
    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
    notificationManager.notify(1, notification);
    startForeground(1, notification);
  }

  private void ensureNotificationChannelExists() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      CharSequence name = "OBSIDIAN_TASK_NOTIFICATIONS_NAME";
      String description ="OBSIDIAN_TASK_NOTIFICATIONS_DESCRIPTION";

      int importance = NotificationManager.IMPORTANCE_DEFAULT;
      NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance);
      channel.setDescription(description);
      NotificationManager notificationManager = getSystemService(NotificationManager.class);
      notificationManager.createNotificationChannel(channel);
    }
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
  }
  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }
}
