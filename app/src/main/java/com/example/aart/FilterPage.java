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

public class FilterPage extends AppCompatActivity {

    RadioGroup speciesGroup, genderGroup;

    RadioButton selectedButton;

    Button applyButton;

    Intent intent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_page);

        speciesGroup = findViewById(R.id.filter_species_group);
        genderGroup = findViewById(R.id.filter_gender_group);



        intent = new Intent(getApplicationContext(),MainActivity.class);

        applyButton = findViewById(R.id.filter_apply_button);

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

               /* if(checkBox1.isChecked())
                {
                    intent.putExtra("Age1","true");
                }
                else
                {
                    intent.putExtra("Age1","false");
                }

                if(checkBox2.isChecked())
                {
                    intent.putExtra("Age2","true");
                }
                else
                {
                    intent.putExtra("Age2","false");
                }


                if(checkBox3.isChecked())
                {
                    intent.putExtra("Age3","true");
                }
                else
                {
                    intent.putExtra("Age3","false");
                }*/

                intent.putExtra("Activity","Filter");

                startActivity(intent);

            }
        });


    }
}