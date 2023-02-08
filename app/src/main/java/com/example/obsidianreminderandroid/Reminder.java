package com.example.obsidianreminderandroid;

import android.util.Pair;

import java.time.LocalDateTime;
import java.util.Date;

public class Reminder {
  public String title;
  public LocalDateTime time;
  private Integer rowNumber;

  public Reminder(String title, LocalDateTime time, Integer rowNumber) {
    this.title = title;
    this.time = time;
    this.rowNumber = rowNumber;
  }
}
