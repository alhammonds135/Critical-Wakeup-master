package com.markm.criticalwakeup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AlarmActive extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_active);

        int hour = getIntent().getIntExtra("hour", 0);
        int min = getIntent().getIntExtra("Min", 0);



        String hourFormatted;
        if(hour > 12) {
            hourFormatted = String.valueOf(hour - 12);
        } else {
            hourFormatted = String.valueOf(hour);
        }

        String minFormatted;
        if (min < 10) {
            minFormatted = "0" + String.valueOf(min);
        } else {
            minFormatted = String.valueOf(min);
        }

        TextView tv = (TextView)findViewById(R.id.fullscreen_content);
        TextView tv2 = findViewById(R.id.textView2);
        tv2.setText(hourFormatted + ":" + minFormatted);
        String s = getIntent().getStringExtra("AlarmName");
        tv.setText(s);
        //TODO make the alarm time appear via intent
        Button close = (Button)findViewById(R.id.dummy_button);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sound = new Intent(getApplicationContext(), SoundService.class);
                stopService(sound);
                Intent mainScreen = new Intent(getApplicationContext(), AlarmPage.class);
                startActivity(mainScreen);
            }
        });
    }
}
