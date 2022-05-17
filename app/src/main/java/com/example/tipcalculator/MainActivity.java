package com.example.tipcalculator;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private final int INIT_TIP_PERCENT = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Make Seekbar and Tip% TextView change with scrubbing
        SeekBar seekbar = findViewById(R.id.seekBarTip);
        TextView tipPercent = findViewById(R.id.tvTipPercent);
        seekbar.setProgress(INIT_TIP_PERCENT);
        tipPercent.setText("15%");
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            // This method gets called when user scrubs the seekbar.
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.i(TAG, "onProgressChanged "+progress);
                tipPercent.setText(progress+"%");
                computeTipAndTotal();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        //Detect change in Base Bill Amount EditText
        EditText baseAmount = findViewById(R.id.etBase);
        baseAmount.addTextChangedListener(new TextWatcher() {
            // This method gets called when user changes the text in Base EditText
            @Override
            public void afterTextChanged(Editable s) {
                Log.i(TAG, "afterTextChanged "+s);
                computeTipAndTotal();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        // Detect change in "Round tip to the nearest $" switch
        Switch roundTip = findViewById(R.id.roundTipSwitch);
        roundTip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i(TAG,"roundTip:onCheckedChanged "+isChecked);
                computeTipAndTotal();
            }
        });

        // Detect change in "Round total to the nearest $" switch
        Switch roundTotal = findViewById(R.id.roundTotalSwitch);
        roundTotal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i(TAG,"roundTotal:onCheckedChanged "+isChecked);
                computeTipAndTotal();
            }
        });
    }

    private void computeTipAndTotal() {
        String strTip;
        String strTotal;
        SeekBar seekbar = findViewById(R.id.seekBarTip);
        EditText baseAmount = findViewById(R.id.etBase);
        TextView tipResult = findViewById(R.id.tvTipAmount);
        TextView totalResult = findViewById(R.id.tvTotalAmount);
        int tip = seekbar.getProgress();
        if(baseAmount.getText().toString().isEmpty()){
            tipResult.setText("0.00");
            totalResult.setText("0.00");
            return;
        }
        // Calculate tip and total
        double baseBill = Double.parseDouble(baseAmount.getText().toString());
        double tipAmount = baseBill * tip / 100;
        double totalAmount = baseBill + tipAmount;
        // Check if Tip Amount needs to be rounded
        Switch tipSwitch = findViewById(R.id.roundTipSwitch);
        if(tipSwitch.isChecked()){
            double roundTipAmount = Math.round(tipAmount);
            strTip = String.format("%.2f",roundTipAmount);

        }else{
            strTip = String.format("%.2f",tipAmount);
        }
        // Check if Total Amount needs to be rounded
        Switch totalSwitch = findViewById(R.id.roundTotalSwitch);
        if(totalSwitch.isChecked()){
            double roundTotalAmount = Math.round(totalAmount);
            strTotal = String.format("%.2f",roundTotalAmount);
        }else {
            strTotal = String.format("%.2f", totalAmount);
        }
        // Set Tip and Total amount TextViews to show results
        tipResult.setText(strTip);
        totalResult.setText(strTotal);
    }
}