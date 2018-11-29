package com.markm.criticalwakeup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;

public class EditAlarm extends AppCompatActivity {

    private EditText aName;
    private TextView crit;
    private Button create, cancel, delete;
    private RadioButton high, med, low;
    private int hour, min, critVal;
    private TimePicker timePicker;
    private Intent alarmService;
    private RadioGroup radioGroup;
    private Switch onOff;
    private boolean on;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_alarm);

        final int index = getIntent().getIntExtra("EDIT", 1);
        SharedPreferences prefs = getSharedPreferences("CriticalWakeup", MODE_PRIVATE);
        String key = "Alarm"+index;
        String json = prefs.getString(key, "");
        Gson gson = new Gson();
        final Alarm alarm = gson.fromJson(json, Alarm.class);

        onOff = findViewById(R.id.onOffSwitch);
        //onOff.setTextOff("Off");
        //onOff.setTextOn("On");
        if(alarm.isOn()) {
            on = true;
            onOff.setChecked(true);
            onOff.setText("On");
        }
        else {
            on = false;
            onOff.setChecked(false);
            onOff.setText("Off");
        }
        onOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!onOff.isChecked()){
                     on = false;
                     onOff.setText("Off");
                }
                else {
                    on = true;
                    onOff.setText("On");
                }
            }
        });

        aName = findViewById(R.id.alarmName);
        aName.setText(alarm.getName());

        crit = findViewById(R.id.level);
        crit.setText("Level:");

        timePicker = findViewById(R.id.timePicker);
        timePicker.setHour(alarm.getHour());
        timePicker.setMinute(alarm.getMinute());

        create = findViewById(R.id.create);
        create.setText("Save Alarm");
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Alarm updatedAlarm = new Alarm(alarm.getId(), aName.getText().toString(),
                        timePicker.getHour(), timePicker.getMinute(), critVal);
                if(on){
                    updatedAlarm.turnOn();
                }
                else{
                    updatedAlarm.turnOff();
                }
                Log.i("edit alarm", updatedAlarm.toString());
                SharedPreferences prefs = getSharedPreferences("CriticalWakeup", MODE_PRIVATE);
                SharedPreferences.Editor edit = prefs.edit();
                Gson gson = new Gson();
                String json = gson.toJson(updatedAlarm);
                String key = "Alarm" + alarm.getId();
                edit.putString(key, json);
                edit.apply();
                Toast.makeText(EditAlarm.this, "Alarm Saved.", Toast.LENGTH_SHORT).show();
                back();

            }
        });


        cancel = findViewById(R.id.cancel);
        cancel.setText("Cancel");
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

        delete = findViewById(R.id.deleteButton);
        delete.setText("Delete");
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = getSharedPreferences("CriticalWakeup", MODE_PRIVATE);
                SharedPreferences.Editor edit = prefs.edit();
                int numOfAlarms = prefs.getInt("numOfAlarms", 0);
                String key = "Alarm" + index;
                edit.remove(key);
                edit.putInt("numOfAlarms", (numOfAlarms-1));
                edit.apply();
                Toast.makeText(EditAlarm.this, "Alarm deleted.", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(EditAlarm.this, AlarmHome.class);
                startActivity(i);
                finish();
            }
        });

        high = findViewById(R.id.high);
        high.setText("High");

        med = findViewById(R.id.medium);
        med.setText("Medium");

        low = findViewById(R.id.low);
        low.setText("Low");

        radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.high)
                    critVal = 3;
                else if(checkedId == R.id.medium)
                    critVal = 2;
                else
                    critVal = 1;
            }
        });
        
        int defaultCrit = alarm.getCritical();
        if(defaultCrit == 3){
            high.setChecked(true);
        }
        else if(defaultCrit == 2) {
            med.setChecked(true);
        }
        else {
            low.setChecked(true);
        }
    }

    public void back(){
        finish();
    }

   // public void make() {

        //saveAlarm(newAlarm); //Save it
    //}
   /* public void saveAlarm(Alarm alarm){

    }*/
}
