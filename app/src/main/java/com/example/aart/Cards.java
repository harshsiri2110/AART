package com.example.aart;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;
import java.util.List;

import com.denzcoskun.imageslider.constants.ScaleTypes;


public class Cards extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);

        ImageSlider imageSlider = findViewById(R.id.image);

        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.dog1, ScaleTypes.CENTER_INSIDE));
        slideModels.add(new SlideModel(R.drawable.dog2, ScaleTypes.CENTER_INSIDE));
        slideModels.add(new SlideModel(R.drawable.dog3, ScaleTypes.CENTER_INSIDE));

        imageSlider.setImageList(slideModels,ScaleTypes.FIT);
    }
}