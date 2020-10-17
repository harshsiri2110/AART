package com.example.aart;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.denzcoskun.imageslider.ImageSlider;
import com.google.firebase.database.DatabaseReference;

public class UpdatePost extends AppCompatActivity {

    EditText txtTitle, txtAge, location, description, medical;
    Button btnupdate, selectImages;
    DatabaseReference reff, reff2;
    String fosterName, timeStamp;

    RadioGroup radioGenderGroup, radioSpeciesGroup;
    RadioButton txtGender, txtSpecies;
    ImageSlider imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_post);

        description = (EditText)findViewById(R.id.update_postDescription);
        medical= (EditText)findViewById(R.id.update_medicalDetails);
        txtTitle = (EditText)findViewById(R.id.update_txttitle);
        txtAge = (EditText)findViewById(R.id.update_txtage);
        location = (EditText)findViewById(R.id.update_location);

        btnupdate = (Button)findViewById(R.id.update_btnupdate);
        selectImages = (Button) findViewById(R.id.update_selectImages);

        radioGenderGroup = (RadioGroup) findViewById(R.id.update_radioGroup);
        radioSpeciesGroup = (RadioGroup) findViewById(R.id.update_speciesSelect);

        imgView = (ImageSlider) findViewById(R.id.update_imgView);

        Bundle bundle = getIntent().getExtras();
    }
}