package com.markm.criticalwakeup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        final int index = getIntent().getIntExtra("EDIT", 1);
        SharedPreferences prefs = getSharedPreferences("CriticalWakeup", MODE_PRIVATE);
        String key = "Alarm"+index;
        String json = prefs.getString(key, "");
        Gson gson = new Gson();
        Alarm alarm = gson.fromJson(json, Alarm.class);
        aName = findViewById(R.id.alarmName);
        //aName.setText(alarm.getName());

        crit = findViewById(R.id.level);
        crit.setText("Level:");

        timePicker = findViewById(R.id.timePicker);
        /**timePicker.setHour(alarm.getHour());
        timePicker.setMinute(alarm.getMinute());

        for making a new alarm there is nothing to set these to so it crashes
        **/

        create = findViewById(R.id.create);
        create.setText("Save Alarm");
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Alarm newAlarm = new Alarm((int) (Math.random() * 1000), aName.getText().toString(),
                        timePicker.getHour(), timePicker.getMinute(), critVal);
                Log.i("add alarm", newAlarm.toString());
                SharedPreferences prefs = getSharedPreferences("CriticalWakeup", MODE_PRIVATE);
                int numOfAlarms = prefs.getInt("numOfAlarms", 0);
                SharedPreferences.Editor edit = prefs.edit();
                Gson gson = new Gson();
                String json = gson.toJson(newAlarm);
                String newName = "Alarm" + index;
                edit.putString(newName, json);
                edit.putInt("numOfAlarms", (numOfAlarms+1));
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

        //radioGroup.check(alarm.getCritical());
    }

    public void back(){
        finish();
    }

}
