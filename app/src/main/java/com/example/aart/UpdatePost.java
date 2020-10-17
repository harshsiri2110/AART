package com.example.aart;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.denzcoskun.imageslider.ImageSlider;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class UpdatePost extends AppCompatActivity {

    EditText txtTitle, txtAge, location, description, medical;
    Button btnupdate, selectImages;
    DatabaseReference reff, reff2;
    String species,title,gender,location_text,age,desc,medical_text,timestamp;

    ArrayList<ImageUrl> imageList = new ArrayList<>();

    RadioButton male,female,dog,cat;
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

        male = findViewById(R.id.update_radioMale);
        female = findViewById(R.id.update_radioFemale);
        dog = findViewById(R.id.update_dogSelect);
        cat = findViewById(R.id.update_CatSelect);

        imgView = (ImageSlider) findViewById(R.id.update_imgView);

        Bundle bundle = getIntent().getExtras();

        species = bundle.getString("species");
        title = bundle.getString("title");
        age = bundle.getString("age");
        gender = bundle.getString("gender");
        location_text = bundle.getString("location");
        medical_text = bundle.getString("medical");
        desc = bundle.getString("desc");
        timestamp = bundle.getString("timestamp");

        description.setText(desc);
        medical.setText(medical_text);
        txtTitle.setText(title);
        txtAge.setText(age);
        location.setText(location_text);



    }
}