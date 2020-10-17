package com.example.aart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UpdatePost extends AppCompatActivity {

    EditText txtTitle, txtAge, location, description, medical;
    Button btnupdate, selectImages;
    DatabaseReference reff;
    String species,title,gender,location_text,age,desc,medical_text,timestamp;

    ArrayList<SlideModel> imageList = new ArrayList<>();

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

        if(species.equals("Dog"))
        {
            dog.toggle();
        }
        else
        {
            cat.toggle();
        }

        if(gender.equals("Male"))
        {
            male.toggle();
        }
        else
        {
            female.toggle();
        }

        reff = FirebaseDatabase.getInstance().getReference().child("Foster").child("Posts").child(timestamp).child("images");

        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot currImg : snapshot.getChildren())
                {
                    String uri = currImg.getValue(ImageUrl.class).getUri();
                    imageList.add(new SlideModel(uri, ScaleTypes.CENTER_INSIDE));


                    if(imageList.size() == snapshot.getChildrenCount())
                    {
                        imgView.setImageList(imageList,ScaleTypes.FIT);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}