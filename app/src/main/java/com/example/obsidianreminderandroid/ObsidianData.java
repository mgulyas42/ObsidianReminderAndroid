package com.example.obsidianreminderandroid;

import android.util.Pair;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ObsidianData {
  public boolean scanned;
  public LinkedHashMap<String, ArrayList<Reminder>> reminders;
  public boolean debug;


  public ObsidianData(boolean scanned, LinkedHashMap<String, ArrayList<Reminder>> reminders, boolean debug) {
    this.scanned = scanned;
    this.reminders = reminders;
    this.debug = debug;
  }
}
