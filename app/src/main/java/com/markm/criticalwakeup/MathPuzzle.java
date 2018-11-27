package com.markm.criticalwakeup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MathPuzzle extends AppCompatActivity {

    int difLevel, i, j, total, inSol;
    String outProb, value;
    TextView prob;
    EditText sol;
    Button chk;

    Random r = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.math_puzzle);

        prob = findViewById(R.id.problem);
        sol = findViewById(R.id.solution);
        chk = findViewById(R.id.check);

        difLevel = 1;

        if (difLevel == 0) {
            i = r.nextInt(9);
            j = r.nextInt(9);
            total = i + j;
        }
        else if (difLevel==1) {
            i = r.nextInt(99);
            j = r.nextInt(99);
            total = i + j;
        }
        else {
            i = r.nextInt(999);
            j = r.nextInt(999);
            total = i + j;
        }

        outProb = i + " + " + j + " = ";
        prob.setText(outProb);

        chk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value = sol.getText().toString();
                inSol = Integer.parseInt(value);

                if(total==inSol){
                    Toast.makeText(MathPuzzle.this, "Correct", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(MathPuzzle.this, "Wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
