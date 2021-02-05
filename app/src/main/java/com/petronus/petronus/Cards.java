package com.petronus.petronus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


public class Cards extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*ImageSlider imageSlider = findViewById(R.id.image);

        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.dog1, ScaleTypes.CENTER_INSIDE));
        slideModels.add(new SlideModel(R.drawable.dog2, ScaleTypes.CENTER_INSIDE));
        slideModels.add(new SlideModel(R.drawable.dog3, ScaleTypes.CENTER_INSIDE));

        imageSlider.setImageList(slideModels,ScaleTypes.FIT);*/
    }
}