package com.example.aart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;

public class FilterPage extends AppCompatActivity {

    RadioGroup speciesGroup, genderGroup;

    RadioButton selectedButton;

    Button applyButton,clearButton;

    SeekBar seekBar;

    Intent intent;

    int progress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_page);

        final LoadingDialog loadingDialog = new LoadingDialog(FilterPage.this);

        speciesGroup = findViewById(R.id.filter_species_group);
        genderGroup = findViewById(R.id.filter_gender_group);

        seekBar = findViewById(R.id.seekBar3);

        intent = new Intent(getApplicationContext(),MainActivity.class);

        applyButton = findViewById(R.id.filter_apply_button);
        clearButton = findViewById(R.id.filter_clear_button);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                // Toast.makeText(getApplicationContext(), i , Toast.LENGTH_SHORT).show();
                progress = i;
                Log.d("TEST", "onProgressChanged: " + i );
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //Toast.makeText(getApplicationContext(), progress , Toast.LENGTH_SHORT).show();
            }
        });

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                applyButton.setEnabled(false);
                loadingDialog.startLoadingDialog();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadingDialog.dismissDialog();
                    }
                }, 2000);

                if(speciesGroup.getCheckedRadioButtonId() != -1) {
                    selectedButton = (RadioButton) findViewById(speciesGroup.getCheckedRadioButtonId());
                    intent.putExtra("Species", selectedButton.getText());
                }
                else
                {
                    intent.putExtra("Species", "-1");
                }

                if(genderGroup.getCheckedRadioButtonId() != -1) {
                    selectedButton = (RadioButton) findViewById(genderGroup.getCheckedRadioButtonId());
                    intent.putExtra("Gender", selectedButton.getText());
                }
                else
                {
                    intent.putExtra("Gender", "-1");
                }

                if(progress == 0)
                {
                    intent.putExtra("Age", "-1");
                }
                else
                {
                    intent.putExtra("Age", progress);
                }

                intent.putExtra("Activity","Filter");
                intent.putExtra("Filter","on");

                startActivity(intent);
                overridePendingTransition(R.anim.stationary_animation,R.anim.slide_out_bottom);

            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("Activity","Filter");
                intent.putExtra("Filter","off");
                startActivity(intent);
                overridePendingTransition(R.anim.stationary_animation,R.anim.slide_out_bottom);
            }
        });


    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stationary_animation,R.anim.slide_out_bottom);
    }
}