package com.example.obsidianreminderandroid

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.util.*
import kotlin.collections.ArrayList

object ActiveReminderManager{

  init {
    println("Singleton class invoked.")
  }
  var list: ArrayList<Int> = ArrayList();
  var counter: Int = 1;

  fun createNotification(reqId: Int) {
    list.add(reqId);
  }

  fun getAllAlarm(): ArrayList<Int> {
    return list;
  }

  fun removeAlarm(id: Int) {
    list.remove(id);
  }

}
