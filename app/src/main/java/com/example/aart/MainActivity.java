package com.example.aart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.animation.ArgbEvaluator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    Adapter adapter;
    Integer[] colours= null;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    List<Model> models;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //startActivity(new Intent(MainActivity.this,Cards.class));

        models = new ArrayList<>();
        models.add(new Model(R.drawable.dog, "Brown and white indie dog", "3 months", "Male", "Kothrud, Pune"));
        models.add(new Model(R.drawable.dog, "Brown indie dog", "4 months", "Female", "Baner, Pune"));
        models.add(new Model(R.drawable.dog, "White indie dog", "1.5 months", "Male", "Warje, Pune"));
        models.add(new Model(R.drawable.dog, "Black indie dog", "2 months", "Female", "Aundh, Pune"));
        adapter = new Adapter(models,this);
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(130,0,130,0);

    }
}