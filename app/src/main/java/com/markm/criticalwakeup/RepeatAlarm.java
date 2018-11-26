package com.markm.criticalwakeup;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class RepeatAlarm extends AppCompatActivity {

    CheckBox mon,tue,wed,thu,fri,sat,sun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repeat_alarm);

        }

    public void onCheckboxClicked(View view) {
        // Is the view checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.mon:
                if (checked)

                    break;
            case R.id.tue:
                if (checked)

                    break;
            case R.id.wed:
                if (checked)

                    break;
            case R.id.thu:
                if (checked)

                    break;
            case R.id.fri:
                if (checked)

                    break;
            case R.id.sat:
                if (checked)

                    break;
            case R.id.sun:
                if (checked)

                    break;
        }
    }
}