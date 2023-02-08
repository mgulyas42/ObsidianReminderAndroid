package com.example.obsidianreminderandroid

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Environment
import android.os.FileObserver
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import java.io.Reader
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class DirectoryFileObserver(path: String, var context: Context) : FileObserver(path, FileObserver.CLOSE_WRITE) {
  var aboslutePath = "";
  var activeReminderManager = ActiveReminderManager;

  init {
    aboslutePath = path
  }

  @SuppressLint("NewApi")
  override fun onEvent(event: Int, path: String?) {
    Log.e("FileObserver: ", "File has been changed!");
    cancelAllNotification();
    try {
      var data = getDataFromDataJSON();
      data.reminders.entries.forEach { it ->
        it.value.forEach { reminder ->
          createNotification(reminder.time.hour, reminder.time.minute, reminder.title);
        }
      }
    } catch (e: java.lang.Exception) {
      throw e
    }
  }
    fun createNotification(hour: Int, minute: Int, text: String) {
      var reqId = ++activeReminderManager.counter;
      val calendar: Calendar = Calendar.getInstance()
      calendar.setTimeInMillis(System.currentTimeMillis())
      calendar.set(Calendar.HOUR_OF_DAY, hour)
      calendar.set(Calendar.MINUTE, minute)
      val notificationIntent = Intent(context, ReminderBroadcast::class.java)
      notificationIntent.putExtra(ReminderBroadcast.NOTIFICATION_CHANNEL_ID, reqId)
      notificationIntent.putExtra(ReminderBroadcast.NOTIFICATION_CHANNEL, text)
      val pendingIntent = PendingIntent.getBroadcast(context, reqId, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
      val alarmManager = (context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?)!!
      alarmManager.set(AlarmManager.RTC_WAKEUP , calendar.timeInMillis , pendingIntent) ;
      this.activeReminderManager.createNotification(reqId);
    }

  fun cancelAllNotification() {
    this.activeReminderManager.getAllAlarm().forEach {
      val notificationIntent = Intent(context, ReminderBroadcast::class.java)
      notificationIntent.putExtra(ReminderBroadcast.NOTIFICATION_CHANNEL_ID, it)
      notificationIntent.putExtra(ReminderBroadcast.NOTIFICATION_CHANNEL, "")
      val pendingIntent = PendingIntent.getBroadcast(context, it, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
      pendingIntent.cancel();
    }
    this.activeReminderManager.list = ArrayList();
  }

  @RequiresApi(Build.VERSION_CODES.O)
  fun getDataFromDataJSON(): ObsidianData {
    val gson = GsonBuilder().registerTypeAdapter(
      LocalDateTime::class.java,
      JsonDeserializer<Any?> { json, typeOfT, context ->
        LocalDateTime.parse(
          json.asString,
          DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        )
      }).create()

    val reader: Reader =
      Files.newBufferedReader(
        Paths.get(
          Environment.getExternalStorageDirectory()
            .toString() + "/Test/Obsidian/SecondBrain/.obsidian/plugins/obsidian-reminder-plugin/data.json"
        )
      );
    val data: ObsidianData = gson.fromJson(reader, ObsidianData::class.java)
    reader.close();
    return data;
  }
}
