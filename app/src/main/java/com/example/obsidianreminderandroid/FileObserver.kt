package com.example.obsidianreminderandroid

import android.R.attr.delay
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.FileObserver
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import com.google.gson.Gson
import java.io.Reader
import java.nio.file.Files
import java.nio.file.Paths
import java.util.Date


class DirectoryFileObserver(path: String, var context: Context) : FileObserver(path) {
  var aboslutePath = "path to your directory"
  var NOTIFICATION_CHANNEL_ID = "OBSIDIAN_TASK_NOTIFICATIONS_ID"

  init {
    aboslutePath = path
  }

  @SuppressLint("NewApi")
  override fun onEvent(event: Int, path: String?) {
    if (path != null) {
      Log.e("FileObserver: ", "File Created");
      // create Gson instance
      try {
        val gson = Gson()
        val reader: Reader = ///storage/0F04-3F0A/Documents/SecondBrain/.obsidian/plugins/obsidian-reminder-plugin/data.json
          Files.newBufferedReader(Paths.get("/storage/emulated/0/test/test/.obsidian/plugins/obsidian-reminder-plugin/data.json"));
            //  "/storage/Documents/SecondBrain/.obsidian/plugins/obsidian-reminder-plugin/data.json"))
        val data: ObsidianData = gson.fromJson(reader, ObsidianData::class.java)

        println(data)

        // close reader
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
          .setSmallIcon(R.drawable.ic_launcher_background)
          .setContentTitle("Obsidian Reminder qqqq")
          .setContentText("background")
          .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        val notification = builder.build()
        var delay = Date().getTime();
        val notificationIntent = Intent(context, ReminderBroadcast::class.java)
        notificationIntent.putExtra(ReminderBroadcast.NOTIFICATION_CHANNEL_ID, 1)
        notificationIntent.putExtra(ReminderBroadcast.NOTIFICATION_CHANNEL, notification)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)
        val alarmManager = (context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?)!!
        alarmManager!![AlarmManager.ELAPSED_REALTIME_WAKEUP,0] = pendingIntent
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP , 0 , pendingIntent) ;
        // close reader
        reader.close()
      } catch(e: java.lang.Exception) {
        println("No file exists" + e.toString());
      }


    }
  }
}
