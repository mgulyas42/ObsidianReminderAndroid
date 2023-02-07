package com.example.obisidanreminder;

import android.util.Pair;

public class ObsidianData {
  public boolean scanned;
  public Object reminders;
  public boolean debug;


  public ObsidianData(boolean scanned, Object reminders, boolean debug) {
    this.scanned = scanned;
    this.reminders = reminders;
    this.debug = debug;
  }
}
