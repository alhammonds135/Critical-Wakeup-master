package com.markm.criticalwakeup;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;


public class AlarmService extends Service {


    private Calendar cal;
    private Intent sound;
    private boolean soundOn = false;
    private boolean isOn;
    private int hour;
    private int min;
    private String Tag = "Alarm Service,";
    private String alarmName;
    private int critNum;
    ArrayList<Alarm> alarmsList;

    class AlarmServiceBinder extends Binder {
        public AlarmService getService(){
            return AlarmService.this;
        }
    }

    private IBinder binder = new AlarmServiceBinder();

    public int onStartCommand(Intent intent, int flags, int startId){
        //make a linked list of all the alarms in the Gson
        Log.i(Tag, "Alarm Service Started");
        alarmsList = new ArrayList<Alarm>();
        SharedPreferences prefs = getSharedPreferences("CriticalWakeup", MODE_PRIVATE);
        int numOfAlarms = prefs.getInt("numOfAlarms", 0);
        Log.i(Tag, "THE NUMBER OF ALARMS IS \n\n" + numOfAlarms);
        Gson gson = new Gson();
        String json = "";
        Alarm alarm;
        for (int i = 1; i <= numOfAlarms; i++){
            Log.i(Tag,"adding alarm to list");
            String key = "Alarm"+i;
            json = prefs.getString(key, "");
            alarm = gson.fromJson(json, Alarm.class);
            alarmsList.add(alarm);
        }
        isOn = true;

        System.out.println("Before new thread");

        new Thread(new Runnable() {
            @Override
            public void run() {
                startService();
            }
        }).start();

        return START_STICKY;
    }

    public void startService(){
        while(isOn){
            //System.out.println("in while loop");
            for (int i = 0; i < alarmsList.size(); i++) {
                //System.out.println("in for loop");
                Alarm currentAlarm = alarmsList.get(i);
                cal = Calendar.getInstance();
                hour = currentAlarm.getHour();
                min = currentAlarm.getMinute();
                int hourLeft = hour - cal.get(Calendar.HOUR_OF_DAY);
                if (hourLeft < 0)
                    hourLeft = hourLeft * -1;
                //Log.i(Tag, "Hour: " + hour + " Calendar Hour: " + cal.get(Calendar.HOUR_OF_DAY) + " Hour Left: " + hourLeft);
                int minLeft = min - cal.get(Calendar.MINUTE);
                //Log.i(Tag, "Min: " + min + " Calendar Min: " + cal.get(Calendar.MINUTE) + " Min Left: " + minLeft);

                //find a way not to have to check everything
                if (hourLeft > 0) {
                    //Log.i(Tag, "More than an hour");
                }
                else if (minLeft > 30) {
                    //Log.i(Tag, "More than 30 min");
                }
                else if (minLeft > 5) {
                    //Log.i(Tag, "More than 5 min");
                }
                else if (minLeft >= 1) {
                    //Log.i(Tag, "More than 1 min");
                }
                else if (currentAlarm.isOn()){
                    Log.i(Tag, "Less than a min");
                    currentAlarm.turnOff();
                    pickPuzzle();
                }
            }
        }
    }

    public void stopService(){
        isOn = false;
    }

    public void pickPuzzle(){
        sound = new Intent(getApplicationContext(), SoundService.class);
        sound.putExtra("crit", critNum);
        startService(sound);
        soundOn = true;
        Random generator = new Random();
        int number = generator.nextInt(2) + 1;

        Intent activeAlarm = new Intent();

        // Here, we are checking to see what the output of the random was
        switch(number) {
            case 1:
                activeAlarm.setClass(getApplicationContext(), MathPuzzle.class);
                activeAlarm.putExtra("difficulty", 1);
                break;
            case 2:
                activeAlarm.setClass(getApplicationContext(), BarcodeActivity.class);
                break;
            default:
                //defaults to math
                activeAlarm.setClass(getApplicationContext(), BarcodeActivity.class);
                break;
        }
        activeAlarm.putExtra("hour", hour);
        activeAlarm.putExtra("Min", min);
        activeAlarm.putExtra("name", alarmName);
        startActivity(activeAlarm);
    }

    @Override
    public void onDestroy() {
        if(soundOn)
            stopService(sound);

        super.onDestroy();
        stopService();
        Log.i(Tag, "Service Destroyed");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}