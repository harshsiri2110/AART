package com.example.aart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Details extends AppCompatActivity {

    DatabaseReference reference;

    int selectedCard = 0;

    Model currModel = new Model();

    ImageSlider imageSlider;

    TextView title, age, gender, location, description, foster_name, foster_number, foster_location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        title = findViewById(R.id.details_title);
        age = findViewById(R.id.ageText);
        gender = findViewById(R.id.genderText);
        location = findViewById(R.id.locationText);
        description = findViewById(R.id.post_desc);

        foster_name = findViewById(R.id.fosterName);
        foster_location = findViewById(R.id.FosterLocation);
        foster_number = findViewById(R.id.FosterNumber);

        imageSlider = findViewById(R.id.images);

        Bundle bundle = getIntent().getExtras();
        selectedCard = bundle.getInt("selectedCard");

        getDetails(selectedCard);

    }

    private void getDetails(int position)
    {
        reference = FirebaseDatabase.getInstance().getReference().child("Foster").child("Posts").child(String.valueOf(position));

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currModel = snapshot.getValue(Model.class);

                snapshot.getRef().child("images").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot imgSnapshot) {

                        int imgCount;
                        imgCount = (int)imgSnapshot.getChildrenCount();
                        //Log.d("TEST","Images count - "+imgCount);
                        List<SlideModel> slideModels = new ArrayList<>();

                        for(DataSnapshot currImgSnap : imgSnapshot.getChildren())
                        {
                            ImageUrl currImg = currImgSnap.getValue(ImageUrl.class);

                            currModel.getImageList().add(currImg);

                            if(currModel.getImageList().size() == imgCount)
                            {
                                title.setText(currModel.getTitle());
                                age.setText(currModel.getAge());
                                gender.setText(currModel.getGender());
                                location.setText(currModel.getLocation());
                                description.setText(currModel.getDescription());

                                for (ImageUrl currUri : currModel.getImageList())
                                {
                                    //Log.d("TEST","Inside Adapter!, Uri - "+currUri.getUri());
                                    slideModels.add(new SlideModel(currUri.getUri(), ScaleTypes.CENTER_INSIDE));

                                    if(slideModels.size() == currModel.getImageList().size())
                                    {
                                        imageSlider.setImageList(slideModels,ScaleTypes.FIT);
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}