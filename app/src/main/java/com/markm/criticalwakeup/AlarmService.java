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

    class AlarmServiceBinder extends Binder {
        public AlarmService getService(){
            return AlarmService.this;
        }
    }

    private IBinder binder = new AlarmServiceBinder();

    public int onStartCommand(Intent intent, int flags, int startId){
        //make a linked list of all the alarms in the Gson
        hour = intent.getExtras().getInt("hour");
        min = intent.getExtras().getInt("min");
        critNum = intent.getExtras().getInt("crit");
        alarmName = intent.getExtras().getString("name");
        isOn = true;

        ArrayList<Alarm> alarmsList = new ArrayList<Alarm>();
        SharedPreferences prefs = getSharedPreferences("CriticalWakeup", MODE_PRIVATE);
        int numOfAlarms = prefs.getInt("numOfAlarms", 0);
        Gson gson = new Gson();
        String json = "";
        Alarm alarm;
        for (int i = 1; i < numOfAlarms; i++){
            String key = "Alarm"+i;
            json = prefs.getString(key, "");
            alarm = gson.fromJson(json, Alarm.class);
            alarmsList.add(alarm);
        }

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
            cal = Calendar.getInstance();
            int hourLeft = hour - cal.get(Calendar.HOUR_OF_DAY);
            if(hourLeft < 0)
                hourLeft = hourLeft * -1;
            Log.i(Tag, "Hour: "+ hour + " Calendar Hour: "+ cal.get(Calendar.HOUR_OF_DAY)+" Hour Left: " + hourLeft);
            int minLeft = min - cal.get(Calendar.MINUTE);
            Log.i(Tag, "Min: "+ min + " Calendar Min: "+ cal.get(Calendar.MINUTE) + " Min Left: " + minLeft);

            if(hourLeft > 0){
                try{
                    Log.i(Tag, "More than an hour");
                    Thread.sleep(1800000);
                } catch (InterruptedException e) {
                    Log.i(Tag, "Thread Interrupted");
                }
            }
            else if( minLeft > 30){
                try{
                    Log.i(Tag, "More than 30 min");
                    Thread.sleep(900000);
                } catch (InterruptedException e) {
                    Log.i(Tag, "Thread Interrupted");
                }
            }
            else if(minLeft > 5){
                try{
                    Log.i(Tag, "More than 5 min");
                    Thread.sleep(300000);
                } catch (InterruptedException e) {
                    Log.i(Tag, "Thread Interrupted");
                }
            }
            else if(minLeft >= 1){
                try{
                    Log.i(Tag, "More than 1 min");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Log.i(Tag, "Thread Interrupted");
                }
            }
            else{
                Log.i(Tag, "Less than a min");
                    isOn = false;
                    pickPuzzle();
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
        // The '2' is the number of activities

        Class activity = null;

        // Here, we are checking to see what the output of the random was
        switch(number) {
            case 1:
                //send Math Puzzle
                //activity = ActivityOne.class;
                break;
            case 2:
                //Send Barcode
                //activity = ActivityTwo.class;
                break;
            default:
                //defaults to math
                //activity = ActivityTree.class;
                break;
        }
        Intent activeAlarm = new Intent(getApplicationContext(), AlarmActive.class);
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
