package com.markm.criticalwakeup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class AlarmPage extends AppCompatActivity {

    Button add;
    Bundle bu;

    @Override
    public Intent getIntent(){
        return super.getIntent();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_page);
        Button button = findViewById(R.id.button1);
        Intent intent = getIntent();
        Bundle extra = intent.getExtras();
        if (extra != null){
            String s = getIntent().getExtras().getString("aName");
            String time = getIntent().getExtras().getString("time");
            String critical = getIntent().getExtras().getString("critical");
            s = s + "\n" + time + "\n" + critical;
            button.setText(s);
            Log.i("Alarm Extra", "Its invisible!");
            button.setVisibility(View.VISIBLE);
        }
        Log.i("Alarm Extra","it skipped the if statement...");

        add = findViewById(R.id.add);
        add.setText("Add Alarm");
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(AlarmPage.this,EditAlarm.class);
                startActivity(in);
            }
        });

        String dirName = "alarms";
        getDir(dirName,0);

//        //Get the number of alarms
//        int numOfAlarms = 4; //Number of alarms to be displayed
//        Button[] array = new Button[numOfAlarms]; //An array to be used to store the buttons created
//        if (numOfAlarms > 0){ //If there are some alarms to be shown
//            for (int i = 0; i < numOfAlarms; i++) {
//                Button button = new Button(this);
//                button.setText("ALARM NAME " + i + "\n" + i +":00AM\nHIGH\nSound1"); //TODO implement the actual pulling of data, instead of hardcoding
//                button.setBackgroundResource(R.drawable.alarm_button_style); //This is a style I created. I'll add it below.
//                array[i] = button; //add it to the array.
//            }
//        }
//
//        //Fill in the table
//        TableLayout table = (TableLayout)findViewById(R.id.alarmSelect);
//        for (int i = 0; i < numOfAlarms; i = i + 2) { //this creates rows and adds them to the table
//            TableRow row = new TableRow(this.getApplicationContext());
//            row.removeAllViews(); //Required by IDE...
//            Button b1 = array[i]; //Add the first array
//            row.addView(b1); //add the button
//            if (array.length < (i+1)) { //if there is another button
//                Button b2 = array[i + 1]; //add the second button on the row
//                row.addView(b2);
//            }
//            table.addView(row); //add the row to the table
//        }
    }
}
