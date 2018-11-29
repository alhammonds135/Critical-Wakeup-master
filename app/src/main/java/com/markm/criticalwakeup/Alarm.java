package com.markm.criticalwakeup;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Scanner;

public class Alarm {

    private int id;
    private String name;
    private int hour;
    private int minute;
    private int critical;
    private boolean on;
    private boolean[] days;
    private int alarmDay;

    public Alarm(int id, String name, int hour, int minute, int critical) {

        this.id = id;
        this.name = name;
        this.hour = hour;
        this.minute = minute;
        this.critical = critical;
        this.on = true;
        this.days = new boolean[7];
        Calendar cal = Calendar.getInstance();
        this.alarmDay = cal.get(Calendar.DAY_OF_WEEK);
    }

    public void saveAlarm() {
        try {
            File file = new File("../alarms.csv");
            file.setWritable(true);
            PrintWriter writer = new PrintWriter(file);
            writer.println(this.id + "," + this.name + "," + this.hour + "," + this.minute + "," + this.critical);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void deleteAlarm() {
        Scanner scanner = new Scanner("alarms.csv");
        while(scanner.hasNextLine()) {

        }
    }

    public boolean[] getDays() {
        return days;
    }

    public int getAlarmDay() {
        return alarmDay;
    }

    public void setAlarmDay(int alarmDay) {
        this.alarmDay = alarmDay;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getMinute() {
        return minute;
    }

    public int getCritical() {
        return critical;
    }

    public int getHour() {
        return hour;
    }

    public boolean isOn() {
        return on;
    }

    public void turnOff(){
        this.on = false;
    }

    public void turnOn(){
        this.on = true;
    }

    public void setDays(boolean[] days) {
        this.days = days;
    }

    @Override
    public String toString() {
        return "Alarm{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", hour=" + hour +
                ", minute=" + minute +
                ", critical=" + critical +
                ", alarm day=" + alarmDay +
                '}';
    }
}
