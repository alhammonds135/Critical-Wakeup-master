package com.markm.criticalwakeup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;

public class AlarmHome extends Activity {

    int numOfAlarms;
    TableLayout table;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_home);


        table = findViewById(R.id.table);
        text = findViewById(R.id.LoadingText);
        FloatingActionButton addBtn = findViewById(R.id.addAlarmBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AlarmHome.this, AddAlarm.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        table.removeAllViews(); //Clear out the table... TODO maybe remove this to not load every time
        loadViews();
    }

    /**
     * This loads the alarms into the table layout
     */
    public void loadViews(){
        SharedPreferences prefs = getSharedPreferences("CriticalWakeup", MODE_PRIVATE);
        numOfAlarms = prefs.getInt("numOfAlarms", 0);
        System.out.println("THE NUMBER OF ALARMS IS \n\n" + numOfAlarms);
        Intent startAlarms = new Intent(this, AlarmService.class);
        if (!(numOfAlarms == 0)){
            startService(startAlarms);
            int index = 1;
            table.setPadding(15, 15, 15, 15);
            while (index <= numOfAlarms)
            {
                TableRow tr = new TableRow(this);
                tr.setWeightSum(100);
                Alarm alarm;
                for (int i = 0; i < 2; i++){
                    if (index <= numOfAlarms) { //Make sure we don't go out of bounds with the second call
                        String key = "Alarm" + index;
                        String json = prefs.getString(key, "");
                        Gson gson =  new Gson();
                        alarm = gson.fromJson(json, Alarm.class);
                        Button button = new Button(this);
                        String mins = String.valueOf(alarm.getMinute());
                        String AmPm = "AM";
                        if (mins.length() == 1){ //fix for 22:1 type times
                            mins = "0"+mins;
                        }
                        //Hour Formatting
                        int hourNum = alarm.getHour();
                        if (hourNum > 12){ //if its 13:00 it will display 1:00 PM
                            hourNum = hourNum - 12;
                            AmPm = "PM";
                        }
                        String hour = String.valueOf(hourNum);
                        if (hour.length() == 1){ //fix for 22:1 type times
                            hour = "0"+hour;
                        }

                        int critical = alarm.getCritical();
                        String criticalLevel = "";
                        switch (critical){
                            case 1: criticalLevel = "Low";
                                break;
                            case 2: criticalLevel = "Mid";
                                break;
                            case 3: criticalLevel = "High";
                                break;
                            default: criticalLevel = "Error";
                                break;
                        }
                        String s = alarm.getName() + "\n" +
                                hour + ":" + mins + " " + AmPm + "\n" +
                                "Level:  " + criticalLevel;
                        System.out.println("Alarm text is " + s);
                        button.setText(s);
                        button.setTextColor(Color.parseColor("#FFFFFF"));
                        TableRow.LayoutParams lp = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        lp.setMargins(10, 10, 10, 10);
                        lp.weight = 50;
                        button.setTag(index);
                        button.setOnClickListener(alarmClicked);
                        if(alarm.isOn()) {
                            if (alarm.getCritical() == 1) {
                                button.setBackgroundResource(R.drawable.alarm_button_low_crit_style);//Custom created style
                            }
                            else if (alarm.getCritical() == 2){
                                button.setBackgroundResource(R.drawable.alarm_button_mid_crit_style);
                            }
                            else {
                                button.setBackgroundResource(R.drawable.alarm_button_high_crit_style);
                            }
                        }
                        else
                            button.setBackgroundResource(R.drawable.alarm_button_off_style);//Custom created style

                        button.setLayoutParams(lp);
                        tr.addView(button);
                        System.out.println("The Alarm Button was added");
                    }
                    index++;
                }
                table.addView(tr);
            }
            text.setVisibility(View.GONE);
        }
        else {
            stopService(startAlarms);
            String s = "There's nothing here!\nClick the alarm icon to add a new alarm!";
            text.setText(s);
        }
    }

    private View.OnClickListener alarmClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int index = (int) v.getTag();
            Intent i = new Intent(AlarmHome.this, EditAlarm.class);
            i.putExtra("EDIT", index);
            startActivity(i);
        }
    };
}
