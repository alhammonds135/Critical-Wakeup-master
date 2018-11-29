package com.markm.criticalwakeup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;

public class AddAlarm extends AppCompatActivity {

    private EditText aName;
    private TextView crit;
    private Button create, cancel;
    private RadioButton high, med, low;
    private int hour, min, critVal;
    private TimePicker timePicker;
    private Intent alarmService;
    private RadioGroup radioGroup;
    private CheckBox sun, mon, tue, wed, thu, fri, sat;
    private boolean[] days;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        days = new boolean[7];
        final int index = getIntent().getIntExtra("EDIT", 1);
        SharedPreferences prefs = getSharedPreferences("CriticalWakeup", MODE_PRIVATE);
        String key = "Alarm"+index;
        String json = prefs.getString(key, "");
        Gson gson = new Gson();
        Alarm alarm = gson.fromJson(json, Alarm.class);
        aName = findViewById(R.id.alarmName);
        aName.setText("Alarm");

        crit = findViewById(R.id.level);
        crit.setText("Level:");

        timePicker = findViewById(R.id.timePicker);

        create = findViewById(R.id.create);
        create.setText("Save Alarm");
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = getSharedPreferences("CriticalWakeup", MODE_PRIVATE);
                int numOfAlarms = prefs.getInt("numOfAlarms", 0);
                numOfAlarms++;
                Alarm newAlarm = new Alarm(numOfAlarms, aName.getText().toString(),
                        timePicker.getHour(), timePicker.getMinute(), critVal);
                newAlarm.setDays(days);
                Log.i("add alarm", newAlarm.toString());
                SharedPreferences.Editor edit = prefs.edit();
                Gson gson = new Gson();
                String json = gson.toJson(newAlarm);
                String newName = "Alarm" + numOfAlarms;
                edit.putString(newName, json);
                edit.putInt("numOfAlarms", (numOfAlarms));
                edit.apply();
                Toast.makeText(AddAlarm.this, "Alarm Saved.", Toast.LENGTH_SHORT).show();
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

        high = findViewById(R.id.high);
        high.setText("High");

        med = findViewById(R.id.medium);
        med.setText("Medium");

        low = findViewById(R.id.low);
        low.setText("Low");

        radioGroup = findViewById(R.id.radioGroup);
        radioGroup.check(R.id.low);
        critVal = 1;
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

        sun = findViewById(R.id.sunCheckBox);
        mon = findViewById(R.id.monCheckBox);
        tue = findViewById(R.id.tueCheckBox);
        wed = findViewById(R.id.wedCheckBox);
        thu = findViewById(R.id.thuCheckBox);
        fri = findViewById(R.id.friCheckBox);
        sat = findViewById(R.id.satCheckBox);

        sun.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    days[0] = true;
                else
                    days[0] = false;
            }
        });

        mon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    days[1] = true;
                else
                    days[1] = false;
            }
        });

        tue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    days[2] = true;
                else
                    days[2] = false;
            }
        });

        wed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    days[3] = true;
                else
                    days[3] = false;
            }
        });

        thu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    days[4] = true;
                else
                    days[4] = false;
            }
        });

        fri.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    days[5] = true;
                else
                    days[5] = false;
            }
        });

        sat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    days[6] = true;
                else
                    days[6] = false;
            }
        });

    }

    public void back(){
        finish();
    }

}
