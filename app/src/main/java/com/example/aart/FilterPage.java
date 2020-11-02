package com.example.aart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

    Button applyButton;

    SeekBar seekBar;

    Intent intent;

    int progress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_page);

        speciesGroup = findViewById(R.id.filter_species_group);
        genderGroup = findViewById(R.id.filter_gender_group);

        seekBar = findViewById(R.id.seekBar3);

        intent = new Intent(getApplicationContext(),MainActivity.class);

        applyButton = findViewById(R.id.filter_apply_button);

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

                intent.putExtra("Activity","Filter");

                startActivity(intent);

            }
        });


    }
}