package com.example.obisidanreminder;

import android.util.Pair;

public class Reminder {
  public String title;
  private String time;
  private Integer rowNumber;

  public Reminder() {

  }

  public Reminder(String title, String time, Integer rowNumber) {
    this.title = title;
    this.time = time;
    this.rowNumber = rowNumber;
  }
}
