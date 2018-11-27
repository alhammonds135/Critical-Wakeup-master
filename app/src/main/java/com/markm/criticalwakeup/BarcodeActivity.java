package com.markm.criticalwakeup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class BarcodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);

        int hour = getIntent().getIntExtra("hour", 0);
        int min = getIntent().getIntExtra("Min", 0);
        String name = getIntent().getStringExtra("name");

        IntentIntegrator integrator = new IntentIntegrator(BarcodeActivity.this);
        integrator.setCaptureActivity(AnyOrientationCaptureActivity.class);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt("Scan something" + "\nCurrent Time is: " + hour + ":" + min);
        integrator.setOrientationLocked(false);
        integrator.setBeepEnabled(false);
        integrator.initiateScan();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //retrieve scan result
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                Intent sound = new Intent(getApplicationContext(), SoundService.class);
                stopService(sound);
                Intent mainScreen = new Intent(getApplicationContext(), AlarmHome.class);
                startActivity(mainScreen);
            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Null", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}
