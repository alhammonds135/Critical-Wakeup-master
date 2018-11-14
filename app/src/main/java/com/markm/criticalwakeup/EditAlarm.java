package com.markm.criticalwakeup;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

public class EditAlarm extends AppCompatActivity {

    private EditText aName;
    private TextView crit;
    private Button create, cancel;
    private RadioButton high, med, low;
    private int hour, min, critVal;
    private TimePicker timePicker;
    private Intent back, alarmService;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_alarm);

        aName = findViewById(R.id.alarmName);

        crit = findViewById(R.id.level);
        crit.setText("Level:");

        timePicker = findViewById(R.id.timePicker);

        create = findViewById(R.id.create);
        create.setText("Create Alarm");
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hour = timePicker.getHour();
                min = timePicker.getMinute();
                alarmService = new Intent(getApplicationContext(), AlarmService.class);
                alarmService.putExtra("hour", hour);
                alarmService.putExtra("min", min);
                alarmService.putExtra("crit", critVal);
                startService(alarmService);
                back = new Intent(EditAlarm.this, AlarmPage.class);
                back.putExtra("sName",aName.getText().toString());
                back.putExtra("critical",crit.getText().toString());
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

    }

    public void back(){
        finish();
    }

    public void make() {
        Alarm newAlarm = new Alarm((int) (Math.random() * 1000), aName.getText().toString(),
                timePicker.getHour(), timePicker.getMinute(), critVal);
        Log.i("new alarm", newAlarm.toString());
        newAlarm.saveAlarm();
    }
}
